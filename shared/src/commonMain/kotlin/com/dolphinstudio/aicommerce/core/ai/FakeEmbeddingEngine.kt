package com.dolphinstudio.aicommerce.core.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.sqrt

/**
 * Deterministic token-hash embedder used by unit tests.
 * The app binds [com.dolphinstudio.aicommerce.core.ai.LiteRtEmbeddingEngine] on Android.
 */
class FakeEmbeddingEngine : EmbeddingEngine {
    override val modelId: String = MODEL_ID

    override suspend fun embed(text: String, purpose: EmbeddingPurpose): FloatArray = withContext(Dispatchers.Default) {
        val vector = FloatArray(DIMENSION)
        val tokens = text.lowercase().split(Regex("[^\\p{L}\\p{N}]+")).filter { it.length > 1 }
        tokens.forEach { token ->
            val index = (token.hashCode() and Int.MAX_VALUE) % DIMENSION
            vector[index] += 1f
        }
        var norm = 0.0
        vector.forEach { value -> norm += value * value }
        if (norm == 0.0) return@withContext vector
        val scale = 1.0 / sqrt(norm)
        FloatArray(DIMENSION) { index -> (vector[index] * scale).toFloat() }
    }

    private companion object {
        const val MODEL_ID = "fake-token-hash"
        const val DIMENSION = 768
    }
}
