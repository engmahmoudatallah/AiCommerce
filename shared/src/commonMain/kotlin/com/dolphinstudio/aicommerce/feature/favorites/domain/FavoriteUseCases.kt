package com.dolphinstudio.aicommerce.feature.favorites.domain

import com.dolphinstudio.aicommerce.feature.product.domain.Product
import kotlinx.coroutines.flow.Flow

class ToggleFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository,
) {
    suspend operator fun invoke(productId: String) = favoriteRepository.toggle(productId)
}

class GetFavoritesUseCase(
    private val favoriteRepository: FavoriteRepository,
) {
    operator fun invoke(): Flow<List<Product>> = favoriteRepository.observeFavorites()

    fun ids(): Flow<Set<String>> = favoriteRepository.observeFavoriteIds()
}
