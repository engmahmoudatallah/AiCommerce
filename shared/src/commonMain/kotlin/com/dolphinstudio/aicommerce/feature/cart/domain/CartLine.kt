package com.dolphinstudio.aicommerce.feature.cart.domain

import com.dolphinstudio.aicommerce.feature.product.domain.Product

data class CartLine(
    val product: Product,
    val quantity: Int,
) {
    val lineTotal: Double = product.price * quantity
}
