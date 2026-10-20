package com.dolphinstudio.aicommerce.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.ColumnTypeConverters

@Database(
    entities = [ProductEntity::class, FavoriteEntity::class, CartItemEntity::class],
    version = 1,
    exportSchema = true,
)
@ColumnTypeConverters(CatalogConverters::class, EmbeddingConverter::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    abstract fun favoriteDao(): FavoriteDao

    abstract fun cartDao(): CartDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
