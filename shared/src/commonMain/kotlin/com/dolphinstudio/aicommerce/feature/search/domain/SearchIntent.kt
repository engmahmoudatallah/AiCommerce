package com.dolphinstudio.aicommerce.feature.search.domain

data class SearchIntent(
    val originalQuery: String,
    val category: String?,
    val minPrice: Double?,
    val maxPrice: Double?,
    val currency: String?,
    val colors: List<String>,
    val brands: List<String>,
    val attributes: List<String>,
    val useCase: String?,
)
