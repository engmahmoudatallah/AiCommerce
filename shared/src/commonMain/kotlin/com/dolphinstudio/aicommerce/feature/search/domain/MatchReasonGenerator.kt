package com.dolphinstudio.aicommerce.feature.search.domain

import com.dolphinstudio.aicommerce.core.common.formatPrice
import com.dolphinstudio.aicommerce.feature.product.domain.Product

class MatchReasonGenerator {
    fun reasons(product: Product, intent: SearchIntent): List<String> {
        val found = mutableListOf<String>()
        val haystack = listOfNotNull(
            product.name,
            product.description,
            product.category,
            product.brand,
            product.color,
        ).plus(product.tags).joinToString(" ").lowercase()

        if (intent.maxPrice != null && product.price <= intent.maxPrice) {
            val currency = intent.currency ?: product.currency
            found += "Within your ${formatPrice(intent.maxPrice, currency)} budget"
        }
        if (intent.colors.any { haystack.contains(it.lowercase()) }) {
            found += "Color matches ${intent.colors.joinToString(", ")}"
        }
        intent.attributes.forEach { attribute ->
            if (haystack.contains(attribute.lowercase())) {
                found += PHRASES[attribute] ?: attribute.replaceFirstChar { it.uppercase() }
            }
        }
        val useCase = intent.useCase
        if (useCase != null && haystack.contains(useCase.lowercase()) && found.none { it.contains(useCase, ignoreCase = true) }) {
            found += PHRASES[useCase] ?: "Suitable for $useCase"
        }
        if (found.isEmpty()) {
            found += "Similar to your description"
        }
        return found
    }

    private companion object {
        val PHRASES = mapOf(
            "wireless" to "Wireless",
            "comfortable" to "Comfortable for long usage",
            "meetings" to "Suitable for meetings",
            "gaming" to "Suitable for gaming",
            "running" to "Suitable for running",
            "walking" to "Suitable for walking",
            "lightweight" to "Lightweight",
            "programming" to "Suitable for programming",
        )
    }
}
