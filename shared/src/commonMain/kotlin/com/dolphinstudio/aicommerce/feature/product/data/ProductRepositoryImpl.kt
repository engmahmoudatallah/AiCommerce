package com.dolphinstudio.aicommerce.feature.product.data

import com.dolphinstudio.aicommerce.core.database.ProductDao
import com.dolphinstudio.aicommerce.feature.product.domain.CatalogRecord
import com.dolphinstudio.aicommerce.feature.product.domain.Product
import com.dolphinstudio.aicommerce.feature.product.domain.ProductRepository
import com.dolphinstudio.aicommerce.feature.search.domain.SearchIntent
import com.dolphinstudio.aicommerce.core.common.IoDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val productDao: ProductDao,
) : ProductRepository {
    override fun observeProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities -> entities.map { it.toProduct() } }
    }

    override suspend fun getProduct(id: String): Product? = withContext(IoDispatcher) {
        productDao.getProductById(id)?.toProduct()
    }

    override suspend fun findCandidates(intent: SearchIntent): List<CatalogRecord> = withContext(IoDispatcher) {
        productDao.findCandidates(
            category = intent.category,
            currency = intent.currency,
            minPrice = intent.minPrice,
            maxPrice = intent.maxPrice,
        ).map { it.toRecord() }
            .filter { record -> matchesColor(record.product, intent) }
            .filter { record -> matchesBrand(record.product, intent) }
    }

    override suspend fun saveEmbedding(productId: String, embedding: FloatArray, modelId: String) {
        withContext(IoDispatcher) {
            productDao.updateEmbedding(productId, embedding.toEmbeddingBytes(), modelId)
        }
    }

    override suspend fun seedIfEmpty() = withContext(IoDispatcher) {
        val existing = productDao.getAllIds().toSet()
        val missing = SampleCatalog.products.filter { it.id !in existing }
        if (missing.isNotEmpty()) {
            productDao.insertProducts(missing.map { it.toEntity() })
        }
        SampleCatalog.products.forEach { product ->
            productDao.updateImageUrl(product.id, product.imageUrl)
        }
    }

    private fun matchesColor(product: Product, intent: SearchIntent): Boolean {
        if (intent.colors.isEmpty()) return true
        val color = product.color?.lowercase() ?: return false
        return intent.colors.any { it.equals(color, ignoreCase = true) }
    }

    private fun matchesBrand(product: Product, intent: SearchIntent): Boolean {
        if (intent.brands.isEmpty()) return true
        val brand = product.brand ?: return false
        return intent.brands.any { it.equals(brand, ignoreCase = true) }
    }
}
