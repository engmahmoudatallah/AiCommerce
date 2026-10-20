package com.dolphinstudio.aicommerce.core.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val quantity: Int,
)
