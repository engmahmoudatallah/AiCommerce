import Foundation
import LiteRTLM
import Shared

final class LiteRtEmbeddingHost: IosLiteRtHost {
    static let shared = LiteRtEmbeddingHost()

    private let minModelBytes: Int64 = 10_000_000
    private let engineStore = EngineStore()

    private init() {}

    func embed(
        modelPath: String,
        prefixedText: String,
        onSuccess: @escaping (KotlinFloatArray) -> Void,
        onError: @escaping (String) -> Void
    ) {
        Task {
            do {
                let engine = try await engineStore.ensureEngine(modelPath: modelPath)
                let response = try await engine.computeEmbedding(contents: [.text(prefixedText)])
                let values = response.embedding
                let array = KotlinFloatArray(size: Int32(values.count))
                for (index, value) in values.enumerated() {
                    array.set(index: Int32(index), value: value)
                }
                onSuccess(array)
            } catch {
                onError(error.localizedDescription)
            }
        }
    }

    func download(
        url: String,
        destination: String,
        onProgress: @escaping (KotlinFloat) -> Void,
        onSuccess: @escaping () -> Void,
        onError: @escaping (String) -> Void
    ) {
        guard let remoteURL = URL(string: url) else {
            onError("Invalid model download URL.")
            return
        }

        let destinationURL = URL(fileURLWithPath: destination)
        let partURL = destinationURL.appendingPathExtension("part")
        let fileManager = FileManager.default

        do {
            if fileManager.fileExists(atPath: partURL.path) {
                try fileManager.removeItem(at: partURL)
            }
        } catch {
            onError(error.localizedDescription)
            return
        }

        let delegate = DownloadDelegate(
            destinationURL: destinationURL,
            partURL: partURL,
            minModelBytes: minModelBytes,
            onProgress: { progress in
                onProgress(KotlinFloat(float: progress))
            },
            onSuccess: onSuccess,
            onError: onError
        )

        let configuration = URLSessionConfiguration.default
        configuration.timeoutIntervalForRequest = 60
        configuration.timeoutIntervalForResource = 60 * 30
        let session = URLSession(configuration: configuration, delegate: delegate, delegateQueue: nil)
        delegate.retainSession(session)
        session.downloadTask(with: remoteURL).resume()
    }

    func close() {
        Task {
            await engineStore.close()
        }
    }
}

private actor EngineStore {
    private var engine: LiteRTLM.EmbeddingEngine?
    private var loadedModelPath: String?

    func ensureEngine(modelPath: String) async throws -> LiteRTLM.EmbeddingEngine {
        if let engine, loadedModelPath == modelPath, await engine.isInitialized() {
            return engine
        }
        await close()
        let cacheDir = URL(fileURLWithPath: modelPath).deletingLastPathComponent().path
        let created = LiteRTLM.EmbeddingEngine(
            config: EmbeddingEngineConfig(
                modelPath: modelPath,
                backend: .cpu(),
                cacheDir: cacheDir
            )
        )
        try await created.initialize()
        engine = created
        loadedModelPath = modelPath
        return created
    }

    func close() async {
        let current = engine
        engine = nil
        loadedModelPath = nil
        if let current {
            await current.close()
        }
    }
}

private final class DownloadDelegate: NSObject, URLSessionDownloadDelegate {
    private let destinationURL: URL
    private let partURL: URL
    private let minModelBytes: Int64
    private let onProgress: (Float) -> Void
    private let onSuccess: () -> Void
    private let onError: (String) -> Void
    private var session: URLSession?

    init(
        destinationURL: URL,
        partURL: URL,
        minModelBytes: Int64,
        onProgress: @escaping (Float) -> Void,
        onSuccess: @escaping () -> Void,
        onError: @escaping (String) -> Void
    ) {
        self.destinationURL = destinationURL
        self.partURL = partURL
        self.minModelBytes = minModelBytes
        self.onProgress = onProgress
        self.onSuccess = onSuccess
        self.onError = onError
    }

    func retainSession(_ session: URLSession) {
        self.session = session
    }

    func urlSession(
        _ session: URLSession,
        downloadTask: URLSessionDownloadTask,
        didWriteData bytesWritten: Int64,
        totalBytesWritten: Int64,
        totalBytesExpectedToWrite: Int64
    ) {
        if totalBytesExpectedToWrite > 0 {
            onProgress(Float(totalBytesWritten) / Float(totalBytesExpectedToWrite))
        } else {
            onProgress(-1)
        }
    }

    func urlSession(
        _ session: URLSession,
        downloadTask: URLSessionDownloadTask,
        didFinishDownloadingTo location: URL
    ) {
        let fileManager = FileManager.default
        do {
            if fileManager.fileExists(atPath: partURL.path) {
                try fileManager.removeItem(at: partURL)
            }
            try fileManager.moveItem(at: location, to: partURL)

            let attributes = try fileManager.attributesOfItem(atPath: partURL.path)
            let size = (attributes[.size] as? NSNumber)?.int64Value ?? 0
            guard size > minModelBytes else {
                try? fileManager.removeItem(at: partURL)
                fail("The on-device model download did not finish.")
                return
            }

            if fileManager.fileExists(atPath: destinationURL.path) {
                try fileManager.removeItem(at: destinationURL)
            }
            try fileManager.moveItem(at: partURL, to: destinationURL)
            onProgress(1)
            onSuccess()
            finish()
        } catch {
            try? fileManager.removeItem(at: partURL)
            fail(error.localizedDescription)
        }
    }

    func urlSession(_ session: URLSession, task: URLSessionTask, didCompleteWithError error: Error?) {
        guard let error else { return }
        try? FileManager.default.removeItem(at: partURL)
        fail(error.localizedDescription)
    }

    private func fail(_ message: String) {
        onError(message)
        finish()
    }

    private func finish() {
        session?.finishTasksAndInvalidate()
        session = nil
    }
}
