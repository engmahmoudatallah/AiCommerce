package com.dolphinstudio.aicommerce.feature.favorites.domain

import com.dolphinstudio.aicommerce.feature.product.domain.Product
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun observeFavorites(): Flow<List<Product>>

    fun observeFavoriteIds(): Flow<Set<String>>

    suspend fun toggle(productId: String)
}
