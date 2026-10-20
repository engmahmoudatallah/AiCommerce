package com.dolphinstudio.aicommerce.feature.search.domain

import com.dolphinstudio.aicommerce.feature.product.domain.Product

data class RankingWeights(
    val semantic: Float = 0.70f,
    val attribute: Float = 0.20f,
    val rating: Float = 0.10f,
)

class ProductRanker(
    private val weights: RankingWeights = RankingWeights(),
) {
    fun attributeMatch(product: Product, intent: SearchIntent): Float {
        val wanted = buildList {
            addAll(intent.attributes)
            addAll(intent.colors)
            addAll(intent.brands)
            intent.useCase?.let(::add)
        }.map { it.lowercase() }.distinct()
        if (wanted.isEmpty()) return 1f
        val haystack = listOfNotNull(
            product.name,
            product.description,
            product.category,
            product.brand,
            product.color,
        ).plus(product.tags).joinToString(" ").lowercase()
        val hits = wanted.count { token -> haystack.contains(token) }
        return hits.toFloat() / wanted.size
    }

    fun score(semanticScore: Float, attributeMatchScore: Float, rating: Double): Float {
        val ratingScore = (rating / 5.0).toFloat().coerceIn(0f, 1f)
        return semanticScore.coerceIn(0f, 1f) * weights.semantic +
            attributeMatchScore.coerceIn(0f, 1f) * weights.attribute +
            ratingScore * weights.rating
    }
}
