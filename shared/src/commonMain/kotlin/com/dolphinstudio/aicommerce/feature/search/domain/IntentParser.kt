package com.dolphinstudio.aicommerce.feature.search.domain

import com.dolphinstudio.aicommerce.feature.product.data.SampleCatalog

class IntentParser(
    private val knownBrands: List<String> = SampleCatalog.brands,
) {
    fun parse(query: String): SearchIntent {
        val normalized = normalize(query)
        val amount = PRICE_PATTERNS.firstNotNullOfOrNull { pattern ->
            pattern.find(normalized)?.groupValues?.getOrNull(1)?.let(::parseAmount)
        }
        return SearchIntent(
            originalQuery = query,
            category = matchCanonical(normalized, CATEGORIES),
            minPrice = null,
            maxPrice = amount,
            currency = currency(normalized),
            colors = matchAll(normalized, COLORS),
            brands = knownBrands.filter { brand -> normalized.contains(normalize(brand)) },
            attributes = matchAll(normalized, ATTRIBUTES),
            useCase = matchCanonical(normalized, USE_CASES),
        )
    }

    private fun currency(query: String): String? = when {
        query.contains("usd") || query.contains("دولار") -> "USD"
        query.contains("egp") || query.contains("جنيه") -> "EGP"
        else -> null
    }

    private fun matchCanonical(query: String, dictionary: Map<String, List<String>>): String? {
        return dictionary.entries.firstOrNull { (_, words) ->
            words.any { word -> query.contains(word) }
        }?.key
    }

    private fun matchAll(query: String, dictionary: Map<String, List<String>>): List<String> {
        return dictionary.mapNotNull { (canonical, words) ->
            canonical.takeIf { words.any { word -> query.contains(word) } }
        }
    }

    private fun parseAmount(raw: String): Double {
        val cleaned = if (THOUSANDS.matches(raw)) raw.replace(",", "") else raw.replace(',', '.')
        return cleaned.toDouble()
    }

    private fun normalize(input: String): String {
        val digits = buildString(input.length) {
            input.forEach { character -> append(EASTERN_DIGITS[character] ?: character) }
        }
        return digits.lowercase()
            .replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('آ', 'ا')
            .replace('ى', 'ي')
            .replace('ة', 'ه')
            .replace(DIACRITICS, "")
    }

    private companion object {
        val PRICE_PATTERNS = listOf(
            Regex("""(?:under|below|less than|max|اقل من|ميزانيتي|متعديش)\s*(\d+(?:[.,]\d+)?)"""),
        )
        val THOUSANDS = Regex("""\d{1,3}(?:,\d{3})+""")
        val DIACRITICS = Regex("[\\u064B-\\u065F]")
        val EASTERN_DIGITS = mapOf(
            '٠' to '0', '١' to '1', '٢' to '2', '٣' to '3', '٤' to '4',
            '٥' to '5', '٦' to '6', '٧' to '7', '٨' to '8', '٩' to '9',
            '۰' to '0', '۱' to '1', '۲' to '2', '۳' to '3', '۴' to '4',
            '۵' to '5', '۶' to '6', '۷' to '7', '۸' to '8', '۹' to '9',
        )
        val CATEGORIES = linkedMapOf(
            "Headphones" to listOf("headphone", "headphones", "سماعه", "سماعات"),
            "Shoes" to listOf("shoe", "shoes", "حذاء", "احذيه"),
            "Laptops" to listOf("laptop", "laptops", "لابتوب", "لاب توب"),
            "Fashion" to listOf("fashion", "clothes", "ملابس", "ازياء"),
            "Accessories" to listOf("accessory", "accessories", "اكسسوار"),
        )
        val COLORS = linkedMapOf(
            "black" to listOf("black", "اسود"),
            "white" to listOf("white", "ابيض"),
            "blue" to listOf("blue", "ازرق"),
        )
        val ATTRIBUTES = linkedMapOf(
            "wireless" to listOf("wireless", "لاسلك"),
            "comfortable" to listOf("comfortable", "مريح"),
            "gaming" to listOf("gaming", "العاب"),
            "meetings" to listOf("meeting", "meetings", "اجتماع"),
            "running" to listOf("running", "جري"),
            "walking" to listOf("walking", "مشي"),
            "lightweight" to listOf("lightweight", "light weight", "خفيف"),
            "programming" to listOf("programming", "برمج"),
        )
        val USE_CASES = linkedMapOf(
            "meetings" to listOf("meeting", "meetings", "اجتماع"),
            "running" to listOf("running", "جري"),
            "walking" to listOf("walking", "مشي"),
            "gaming" to listOf("gaming", "العاب"),
            "programming" to listOf("programming", "برمج"),
        )
    }
}
