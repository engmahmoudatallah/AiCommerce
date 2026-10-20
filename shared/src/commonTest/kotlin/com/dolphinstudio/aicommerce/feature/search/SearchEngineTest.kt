package com.dolphinstudio.aicommerce.feature.search.domain

import com.dolphinstudio.aicommerce.core.ai.EmbeddingEngine
import com.dolphinstudio.aicommerce.core.ai.EmbeddingPurpose
import com.dolphinstudio.aicommerce.core.ai.cosineSimilarity
import com.dolphinstudio.aicommerce.feature.product.domain.CatalogRecord
import com.dolphinstudio.aicommerce.feature.product.domain.Product
import com.dolphinstudio.aicommerce.feature.product.domain.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IntentParserTest {
    private val parser = IntentParser(knownBrands = listOf("WorkSound"))

    @Test
    fun parsesEnglishBudgetQuery() {
        val intent = parser.parse("wireless headphones under 5000 EGP")

        assertEquals("Headphones", intent.category)
        assertEquals(5000.0, intent.maxPrice)
        assertEquals("EGP", intent.currency)
        assertTrue(intent.attributes.contains("wireless"))
    }

    @Test
    fun parsesArabicBudgetWithEasternDigits() {
        val intent = parser.parse("سماعات لاسلكية اقل من ٥٠٠٠ جنيه")

        assertEquals("Headphones", intent.category)
        assertEquals(5000.0, intent.maxPrice)
        assertEquals("EGP", intent.currency)
        assertTrue(intent.attributes.contains("wireless"))
    }
}

class CosineSimilarityTest {
    @Test
    fun identicalVectorsScoreOne() {
        val vector = floatArrayOf(1f, 2f, 3f)
        assertEquals(1f, cosineSimilarity(vector, vector), 0.0001f)
    }

    @Test
    fun orthogonalVectorsScoreZero() {
        val score = cosineSimilarity(floatArrayOf(1f, 0f), floatArrayOf(0f, 1f))
        assertEquals(0f, score, 0.0001f)
    }

    @Test
    fun zeroVectorScoresZero() {
        assertEquals(0f, cosineSimilarity(floatArrayOf(0f, 0f), floatArrayOf(1f, 1f)), 0.0001f)
    }
}

class ProductRankerTest {
    private val ranker = ProductRanker()

    @Test
    fun appliesPublishedWeights() {
        val score = ranker.score(semanticScore = 1f, attributeMatchScore = 0f, rating = 0.0)
        assertEquals(0.70f, score, 0.0001f)
    }
}

class MatchReasonGeneratorTest {
    @Test
    fun explainsBudgetAndAttributes() {
        val product = sampleProduct(price = 4200.0, tags = listOf("wireless", "meetings", "comfortable"))
        val intent = SearchIntent(
            originalQuery = "wireless comfortable headphones for meetings under 5000 EGP",
            category = "Headphones",
            minPrice = null,
            maxPrice = 5000.0,
            currency = "EGP",
            colors = emptyList(),
            brands = emptyList(),
            attributes = listOf("wireless", "comfortable", "meetings"),
            useCase = "meetings",
        )

        val reasons = MatchReasonGenerator().reasons(product, intent)

        assertTrue(reasons.any { it.contains("5,000 EGP") })
        assertTrue(reasons.any { it.contains("Wireless") })
        assertTrue(reasons.any { it.contains("meetings") })
        assertTrue(reasons.any { it.contains("Comfortable") })
    }
}

class SearchProductsUseCaseTest {
    @Test
    fun filtersBudgetAndRanksCloserEmbeddingFirst() = runBlocking {
        val queryVector = floatArrayOf(1f, 0f, 0f)
        val close = sampleProduct(id = "close", name = "WorkSound Pro", price = 4200.0, tags = listOf("wireless"))
        val far = sampleProduct(id = "far", name = "Studio Wire", price = 2200.0, tags = listOf("wired"))
        val expensive = sampleProduct(id = "expensive", name = "Arena Blast", price = 9000.0, tags = listOf("wireless"))
        val repository = MemoryProductRepository(
            listOf(
                CatalogRecord(close, queryVector, "fixed"),
                CatalogRecord(far, floatArrayOf(0f, 1f, 0f), "fixed"),
                CatalogRecord(expensive, queryVector, "fixed"),
            ),
        )
        val engine = FixedEmbeddingEngine(queryVector)
        val useCase = SearchProductsUseCase(
            intentParser = IntentParser(knownBrands = emptyList()),
            searchEngine = SemanticSearchEngine(
                productRepository = repository,
                embeddingEngine = engine,
                ranker = ProductRanker(),
                reasonGenerator = MatchReasonGenerator(),
            ),
        )

        val outcome = useCase("wireless headphones under 5000 EGP")

        assertEquals(listOf("close", "far"), outcome.results.map { it.product.id })
        assertTrue(outcome.results.first().matchPercentage >= outcome.results.last().matchPercentage)
    }
}

private class FixedEmbeddingEngine(
    private val vector: FloatArray,
) : EmbeddingEngine {
    override val modelId: String = "fixed"

    override suspend fun embed(text: String, purpose: EmbeddingPurpose): FloatArray = vector
}

private class MemoryProductRepository(
    private val records: List<CatalogRecord>,
) : ProductRepository {
    override fun observeProducts(): Flow<List<Product>> = flowOf(records.map { it.product })

    override suspend fun getProduct(id: String): Product? = records.firstOrNull { it.product.id == id }?.product

    override suspend fun findCandidates(intent: SearchIntent): List<CatalogRecord> {
        return records.filter { record ->
            val product = record.product
            (intent.category == null || product.category.equals(intent.category, ignoreCase = true)) &&
                (intent.maxPrice == null || product.price <= intent.maxPrice) &&
                (intent.currency == null || product.currency == intent.currency)
        }
    }

    override suspend fun saveEmbedding(productId: String, embedding: FloatArray, modelId: String) = Unit

    override suspend fun seedIfEmpty() = Unit
}

private fun sampleProduct(
    id: String = "headphone_01",
    name: String = "WorkSound Pro",
    price: Double = 4200.0,
    tags: List<String> = listOf("wireless"),
) = Product(
    id = id,
    name = name,
    description = "Comfortable wireless headphones for meetings.",
    category = "Headphones",
    subcategory = null,
    price = price,
    currency = "EGP",
    brand = "WorkSound",
    color = "black",
    rating = 4.0,
    imageUrl = "",
    tags = tags,
)
