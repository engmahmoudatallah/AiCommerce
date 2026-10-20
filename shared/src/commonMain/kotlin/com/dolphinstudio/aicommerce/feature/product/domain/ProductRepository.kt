package com.dolphinstudio.aicommerce.feature.product.domain

import com.dolphinstudio.aicommerce.feature.search.domain.SearchIntent
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(): Flow<List<Product>>

    suspend fun getProduct(id: String): Product?

    suspend fun findCandidates(intent: SearchIntent): List<CatalogRecord>

    suspend fun saveEmbedding(productId: String, embedding: FloatArray, modelId: String)

    suspend fun seedIfEmpty()
}
