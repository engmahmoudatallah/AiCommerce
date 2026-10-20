package com.dolphinstudio.aicommerce.core.ai

import com.dolphinstudio.aicommerce.core.common.iosDocumentsDirectory
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileSize
import platform.Foundation.NSNumber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(ExperimentalForeignApi::class)
class IosModelManager : ModelManager {
    private val modelPath = iosDocumentsDirectory() + "/" + EmbeddingModels.FILE_NAME
    private val progress = MutableStateFlow<Float?>(null)

    override val downloadProgress: StateFlow<Float?> = progress.asStateFlow()

    override suspend fun checkIfModelAvailable(): Boolean = isUsable(modelPath)

    override suspend fun downloadModel() {
        if (checkIfModelAvailable()) {
            progress.value = null
            return
        }
        val host = IosLiteRtBridge.host
            ?: error("LiteRT-LM host is not registered.")
        progress.value = 0f
        try {
            suspendCancellableCoroutine { continuation ->
                host.download(
                    EmbeddingModels.DOWNLOAD_URL,
                    modelPath,
                    { value -> progress.value = value },
                    {
                        progress.value = 1f
                        continuation.resume(Unit)
                    },
                    { message ->
                        continuation.resumeWithException(IllegalStateException(message))
                    },
                )
            }
            if (!isUsable(modelPath)) {
                NSFileManager.defaultManager.removeItemAtPath(modelPath, null)
                error("The on-device model download did not finish.")
            }
        } finally {
            progress.value = null
        }
    }

    override suspend fun modelFilePath(): String {
        check(checkIfModelAvailable()) { "Download the on-device model before searching." }
        return modelPath
    }

    override suspend fun deleteModel() {
        IosLiteRtBridge.host?.close()
        NSFileManager.defaultManager.removeItemAtPath(modelPath, null)
        progress.value = null
    }

    private fun isUsable(path: String): Boolean {
        val manager = NSFileManager.defaultManager
        if (!manager.fileExistsAtPath(path)) return false
        val attributes = manager.attributesOfItemAtPath(path, null) ?: return false
        val size = (attributes[NSFileSize] as? NSNumber)?.longValue ?: return false
        return size > MIN_MODEL_BYTES
    }

    private companion object {
        const val MIN_MODEL_BYTES = 10_000_000L
    }
}
