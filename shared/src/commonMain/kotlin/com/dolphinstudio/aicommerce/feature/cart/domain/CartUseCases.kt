package com.dolphinstudio.aicommerce.feature.cart.domain

import kotlinx.coroutines.flow.Flow

class AddToCartUseCase(
    private val cartRepository: CartRepository,
) {
    suspend operator fun invoke(productId: String) = cartRepository.add(productId)
}

class GetCartUseCase(
    private val cartRepository: CartRepository,
) {
    operator fun invoke(): Flow<List<CartLine>> = cartRepository.observeCart()

    suspend fun updateQuantity(productId: String, quantity: Int) {
        cartRepository.updateQuantity(productId, quantity)
    }
}
