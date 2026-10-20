package com.dolphinstudio.aicommerce.core.ai

import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.EmbeddingEngineConfig
import com.google.ai.edge.litertlm.InputData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import com.google.ai.edge.litertlm.EmbeddingEngine as LiteRtEngine

class LiteRtEmbeddingEngine(
    private val modelManager: ModelManager,
) : EmbeddingEngine {
    override val modelId: String = EmbeddingModels.GEMMA_TEXT_270M

    private val mutex = Mutex()
    private var nativeEngine: LiteRtEngine? = null
    private var loadedStamp: String? = null

    override suspend fun embed(text: String, purpose: EmbeddingPurpose): FloatArray = withContext(Dispatchers.IO) {
        mutex.withLock {
            val engine = loadEngine()
            val input = EmbeddingModels.prefix(text, purpose)
            val embedding = engine.computeEmbedding(listOf(InputData.Text(input))).embedding
            FloatArray(embedding.size) { index -> embedding[index] }
        }
    }

    private suspend fun loadEngine(): LiteRtEngine {
        if (!modelManager.checkIfModelAvailable()) {
            releaseQuietly()
            error("Download the on-device model before searching.")
        }
        val filePath = modelManager.modelFilePath()
        val file = File(filePath)
        val stamp = "${file.length()}:${file.lastModified()}"
        val current = nativeEngine
        if (current != null && loadedStamp == stamp) return current
        releaseQuietly()
        val config = EmbeddingEngineConfig(
            modelPath = filePath,
            backend = Backend.CPU(),
            cacheDir = file.parentFile?.absolutePath,
        )
        val created = LiteRtEngine(config)
        created.initialize()
        nativeEngine = created
        loadedStamp = stamp
        return created
    }

    private fun releaseQuietly() {
        nativeEngine?.close()
        nativeEngine = null
        loadedStamp = null
    }
}
