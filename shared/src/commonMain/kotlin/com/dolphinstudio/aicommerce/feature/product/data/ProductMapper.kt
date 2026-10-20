package com.dolphinstudio.aicommerce.feature.product.data

import com.dolphinstudio.aicommerce.core.database.EmbeddingConverter
import com.dolphinstudio.aicommerce.core.database.ProductEntity
import com.dolphinstudio.aicommerce.feature.product.domain.CatalogRecord
import com.dolphinstudio.aicommerce.feature.product.domain.Product

private val embeddingConverter = EmbeddingConverter()

fun ProductEntity.toProduct(): Product = Product(
    id = id,
    name = name,
    description = description,
    category = category,
    subcategory = subcategory,
    price = price,
    currency = currency,
    brand = brand,
    color = color,
    rating = rating,
    imageUrl = imageUrl,
    tags = tags,
)

fun ProductEntity.toRecord(): CatalogRecord = CatalogRecord(
    product = toProduct(),
    embedding = embeddingConverter.toFloatArray(embedding).takeIf { it.isNotEmpty() },
    embeddingModel = embeddingModel,
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    description = description,
    category = category,
    subcategory = subcategory,
    price = price,
    currency = currency,
    brand = brand,
    color = color,
    rating = rating,
    imageUrl = imageUrl,
    tags = tags,
    embedding = ByteArray(0),
    embeddingModel = "",
)

fun FloatArray.toEmbeddingBytes(): ByteArray = embeddingConverter.fromFloatArray(this)
