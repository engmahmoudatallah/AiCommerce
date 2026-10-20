package com.dolphinstudio.aicommerce.feature.favorites.data

import com.dolphinstudio.aicommerce.core.database.FavoriteDao
import com.dolphinstudio.aicommerce.core.database.FavoriteEntity
import com.dolphinstudio.aicommerce.core.database.ProductDao
import com.dolphinstudio.aicommerce.feature.favorites.domain.FavoriteRepository
import com.dolphinstudio.aicommerce.core.common.IoDispatcher
import com.dolphinstudio.aicommerce.feature.product.data.toProduct
import com.dolphinstudio.aicommerce.feature.product.domain.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao,
    private val productDao: ProductDao,
) : FavoriteRepository {
    override fun observeFavorites(): Flow<List<Product>> {
        return combine(favoriteDao.observeAll(), productDao.getAllProducts()) { favorites, products ->
            val ids = favorites.map { it.productId }.toSet()
            products.filter { it.id in ids }.map { it.toProduct() }
        }
    }

    override fun observeFavoriteIds(): Flow<Set<String>> {
        return favoriteDao.observeAll().map { favorites -> favorites.map { it.productId }.toSet() }
    }

    override suspend fun toggle(productId: String) = withContext(IoDispatcher) {
        if (favoriteDao.isFavorite(productId)) {
            favoriteDao.delete(productId)
        } else {
            favoriteDao.insert(FavoriteEntity(productId))
        }
    }
}
