package com.dolphinstudio.aicommerce.feature.search.domain

import com.dolphinstudio.aicommerce.core.ai.EmbeddingEngine
import com.dolphinstudio.aicommerce.core.ai.EmbeddingPurpose
import com.dolphinstudio.aicommerce.core.ai.cosineSimilarity
import com.dolphinstudio.aicommerce.feature.product.domain.ProductRepository
import com.dolphinstudio.aicommerce.feature.product.domain.toEmbeddingText
import kotlin.math.roundToInt

class SemanticSearchEngine(
    private val productRepository: ProductRepository,
    private val embeddingEngine: EmbeddingEngine,
    private val ranker: ProductRanker,
    private val reasonGenerator: MatchReasonGenerator,
) {
    suspend fun search(query: String, intent: SearchIntent): List<SearchResult> {
        val candidates = productRepository.findCandidates(intent)
        val prepared = candidates.map { record ->
            val current = record.embedding
            if (current != null && record.embeddingModel == embeddingEngine.modelId) {
                record.product to current
            } else {
                val vector = embeddingEngine.embed(record.product.toEmbeddingText(), EmbeddingPurpose.Document)
                productRepository.saveEmbedding(record.product.id, vector, embeddingEngine.modelId)
                record.product to vector
            }
        }
        if (prepared.isEmpty()) return emptyList()
        val queryVector = embeddingEngine.embed(semanticText(query), EmbeddingPurpose.Query)
        return prepared.map { (product, vector) ->
            val semantic = cosineSimilarity(queryVector, vector).coerceIn(-1f, 1f)
            val boundedSemantic = semantic.coerceIn(0f, 1f)
            val attributeScore = ranker.attributeMatch(product, intent)
            val finalScore = ranker.score(boundedSemantic, attributeScore, product.rating)
            SearchResult(
                product = product,
                similarityScore = semantic,
                matchPercentage = (finalScore * 100).roundToInt().coerceIn(0, 100),
                reasons = reasonGenerator.reasons(product, intent),
            )
        }.sortedByDescending { it.matchPercentage }
    }

    private fun semanticText(query: String): String {
        return query
            .replace(PRICE_CLAUSE, " ")
            .replace(CURRENCY, " ")
            .replace(Regex("\\s+"), " ")
            .trim()
            .ifBlank { query }
    }

    private companion object {
        val PRICE_CLAUSE = Regex(
            """(?i)(?:under|below|less than|max|اقل من|أقل من|ميزانيتي|متعديش)\s*[0-9٠-٩۰-۹]+(?:[.,][0-9٠-٩۰-۹]+)?""",
        )
        val CURRENCY = Regex("""(?i)\b(EGP|USD)\b|جنيه|دولار""")
    }
}
