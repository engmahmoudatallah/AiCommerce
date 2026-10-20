package com.dolphinstudio.aicommerce.core.database

import androidx.room3.ColumnTypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

private val catalogJson = Json { ignoreUnknownKeys = true }

class CatalogConverters {
    @ColumnTypeConverter
    fun fromStringList(value: List<String>): String {
        return catalogJson.encodeToString(ListSerializer(String.serializer()), value)
    }

    @ColumnTypeConverter
    fun toStringList(value: String): List<String> {
        if (value.isBlank()) return emptyList()
        return catalogJson.decodeFromString(ListSerializer(String.serializer()), value)
    }
}

class EmbeddingConverter {
    @ColumnTypeConverter
    fun fromFloatArray(value: FloatArray?): ByteArray {
        if (value == null || value.isEmpty()) return ByteArray(0)
        val bytes = ByteArray(value.size * 4)
        value.forEachIndexed { index, number ->
            val bits = number.toRawBits()
            val offset = index * 4
            bytes[offset] = (bits and 0xFF).toByte()
            bytes[offset + 1] = ((bits ushr 8) and 0xFF).toByte()
            bytes[offset + 2] = ((bits ushr 16) and 0xFF).toByte()
            bytes[offset + 3] = ((bits ushr 24) and 0xFF).toByte()
        }
        return bytes
    }

    @ColumnTypeConverter
    fun toFloatArray(value: ByteArray?): FloatArray {
        if (value == null || value.isEmpty() || value.size % 4 != 0) return FloatArray(0)
        return FloatArray(value.size / 4) { index ->
            val offset = index * 4
            val bits = (value[offset].toInt() and 0xFF) or
                ((value[offset + 1].toInt() and 0xFF) shl 8) or
                ((value[offset + 2].toInt() and 0xFF) shl 16) or
                ((value[offset + 3].toInt() and 0xFF) shl 24)
            Float.fromBits(bits)
        }
    }
}
