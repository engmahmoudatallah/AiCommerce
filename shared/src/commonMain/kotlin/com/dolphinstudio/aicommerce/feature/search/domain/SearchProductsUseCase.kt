package com.dolphinstudio.aicommerce.feature.search.domain

class SearchProductsUseCase(
    private val intentParser: IntentParser,
    private val searchEngine: SemanticSearchEngine,
) {
    suspend operator fun invoke(query: String): SearchOutcome {
        val intent = intentParser.parse(query)
        val results = searchEngine.search(query, intent)
        return SearchOutcome(intent = intent, results = results)
    }
}
