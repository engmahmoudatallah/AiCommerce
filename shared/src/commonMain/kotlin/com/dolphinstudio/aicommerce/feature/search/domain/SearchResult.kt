package com.dolphinstudio.aicommerce.feature.search.domain

import com.dolphinstudio.aicommerce.feature.product.domain.Product

data class SearchResult(
    val product: Product,
    val similarityScore: Float,
    val matchPercentage: Int,
    val reasons: List<String>,
)

data class SearchOutcome(
    val intent: SearchIntent,
    val results: List<SearchResult>,
)
