package com.dolphinstudio.aicommerce.feature.cart.data

import com.dolphinstudio.aicommerce.core.database.CartDao
import com.dolphinstudio.aicommerce.core.database.CartItemEntity
import com.dolphinstudio.aicommerce.core.database.ProductDao
import com.dolphinstudio.aicommerce.core.common.IoDispatcher
import com.dolphinstudio.aicommerce.feature.cart.domain.CartLine
import com.dolphinstudio.aicommerce.feature.cart.domain.CartRepository
import com.dolphinstudio.aicommerce.feature.product.data.toProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class CartRepositoryImpl(
    private val cartDao: CartDao,
    private val productDao: ProductDao,
) : CartRepository {
    override fun observeCart(): Flow<List<CartLine>> {
        return combine(cartDao.observeAll(), productDao.getAllProducts()) { items, products ->
            val byId = products.associateBy { it.id }
            items.mapNotNull { item ->
                val product = byId[item.productId]?.toProduct() ?: return@mapNotNull null
                CartLine(product = product, quantity = item.quantity)
            }
        }
    }

    override suspend fun add(productId: String) = withContext(IoDispatcher) {
        val current = cartDao.get(productId)
        val quantity = (current?.quantity ?: 0) + 1
        cartDao.insert(CartItemEntity(productId = productId, quantity = quantity))
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) = withContext(IoDispatcher) {
        if (quantity <= 0) {
            cartDao.delete(productId)
        } else {
            cartDao.insert(CartItemEntity(productId = productId, quantity = quantity))
        }
    }
}
