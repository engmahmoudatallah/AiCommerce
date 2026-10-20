package com.dolphinstudio.aicommerce.feature.product.domain

data class Product(
    val id: String,
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
)

fun Product.toEmbeddingText(): String {
    return """
        $name.
        $description.
        Category: $category.
        Brand: ${brand.orEmpty()}.
        Tags: ${tags.joinToString()}
    """.trimIndent()
}

class CatalogRecord(
    val product: Product,
    val embedding: FloatArray?,
    val embeddingModel: String,
)
