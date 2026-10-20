package com.dolphinstudio.aicommerce.core.ai

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IosLiteRtEmbeddingEngine(
    private val modelManager: ModelManager,
) : EmbeddingEngine {
    override val modelId: String = EmbeddingModels.GEMMA_TEXT_270M

    private val mutex = Mutex()

    override suspend fun embed(text: String, purpose: EmbeddingPurpose): FloatArray = mutex.withLock {
        val host = IosLiteRtBridge.host
            ?: error("LiteRT-LM host is not registered.")
        if (!modelManager.checkIfModelAvailable()) {
            error("Download the on-device model before searching.")
        }
        val modelPath = modelManager.modelFilePath()
        val prefixed = EmbeddingModels.prefix(text, purpose)
        suspendCoroutine { continuation ->
            host.embed(
                modelPath,
                prefixed,
                { embedding -> continuation.resume(embedding) },
                { message -> continuation.resumeWithException(IllegalStateException(message)) },
            )
        }
    }
}
