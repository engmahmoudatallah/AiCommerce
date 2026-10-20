package com.dolphinstudio.aicommerce.feature.cart.domain

import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCart(): Flow<List<CartLine>>

    suspend fun add(productId: String)

    suspend fun updateQuantity(productId: String, quantity: Int)
}
