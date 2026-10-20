package com.dolphinstudio.aicommerce.core.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val category: String,
    val subcategory: String?,
    val price: Double,
    val currency: String,
    val brand: String?,
    val color: String?,
    val rating: Double,
    val imageUrl: String,
    val tags: List<String>,
    val embedding: ByteArray,
    val embeddingModel: String,
)
