package com.dolphinstudio.aicommerce.core.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val productId: String,
)
