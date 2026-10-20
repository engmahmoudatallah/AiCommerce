package com.dolphinstudio.aicommerce.core.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: String): ProductEntity?

    @Query("SELECT * FROM products WHERE LOWER(category) = LOWER(:category)")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE price <= :maxPrice")
    suspend fun getProductsUnderPrice(maxPrice: Double): List<ProductEntity>

    @Query(
        """
        SELECT * FROM products
        WHERE (:category IS NULL OR LOWER(category) = LOWER(:category))
        AND (:currency IS NULL OR currency = :currency)
        AND (:minPrice IS NULL OR price >= :minPrice)
        AND (:maxPrice IS NULL OR price <= :maxPrice)
        """,
    )
    suspend fun findCandidates(
        category: String?,
        currency: String?,
        minPrice: Double?,
        maxPrice: Double?,
    ): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Query("SELECT id FROM products")
    suspend fun getAllIds(): List<String>

    @Query("UPDATE products SET imageUrl = :imageUrl WHERE id = :id AND imageUrl = ''")
    suspend fun updateImageUrl(id: String, imageUrl: String)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    @Query(
        """
        UPDATE products
        SET embedding = :embedding, embeddingModel = :model
        WHERE id = :id
        """,
    )
    suspend fun updateEmbedding(id: String, embedding: ByteArray, model: String)
}
