package com.dolphinstudio.aicommerce.core.common

fun formatPrice(price: Double, currency: String): String {
    val rounded = if (price % 1.0 == 0.0) price.toLong().toString() else {
        val whole = price.toLong()
        val fraction = kotlin.math.round((price - whole) * 100).toInt().toString().padStart(2, '0')
        "$whole.$fraction"
    }
    val parts = rounded.split('.')
    val grouped = parts[0].reversed().chunked(3).joinToString(",").reversed()
    val number = if (parts.size == 2) "$grouped.${parts[1]}" else grouped
    return "$number $currency"
}
