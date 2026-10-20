package com.dolphinstudio.aicommerce.feature.search.presentation

import com.dolphinstudio.aicommerce.feature.search.domain.SearchIntent
import com.dolphinstudio.aicommerce.feature.search.domain.SearchResult

class SearchMemory {
    var query: String = ""
    var selectedProductId: String = ""
    var intent: SearchIntent? = null
    var results: List<SearchResult> = emptyList()
}
