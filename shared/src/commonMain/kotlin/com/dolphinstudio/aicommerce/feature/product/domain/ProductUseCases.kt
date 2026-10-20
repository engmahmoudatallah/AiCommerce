package com.dolphinstudio.aicommerce.feature.product.domain

import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val productRepository: ProductRepository,
) {
    fun recommended(): Flow<List<Product>> = productRepository.observeProducts()
}

class GetProductDetailsUseCase(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke(id: String): Product? = productRepository.getProduct(id)
}

class SeedCatalogUseCase(
    private val productRepository: ProductRepository,
) {
    suspend operator fun invoke() = productRepository.seedIfEmpty()
}
