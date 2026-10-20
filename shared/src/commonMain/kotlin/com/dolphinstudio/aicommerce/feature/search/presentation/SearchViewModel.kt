package com.dolphinstudio.aicommerce.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolphinstudio.aicommerce.core.ai.ModelManager
import com.dolphinstudio.aicommerce.feature.search.domain.SearchIntent
import com.dolphinstudio.aicommerce.feature.search.domain.SearchProductsUseCase
import com.dolphinstudio.aicommerce.feature.search.domain.SearchResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val loading: Boolean = false,
    val intent: SearchIntent? = null,
    val results: List<SearchResult> = emptyList(),
    val error: String? = null,
    val needsModel: Boolean = false,
    val downloadProgress: Float? = null,
)

class SearchViewModel(
    private val searchProducts: SearchProductsUseCase,
    private val modelManager: ModelManager,
    private val searchMemory: SearchMemory,
) : ViewModel() {
    private val uiState = MutableStateFlow(SearchUiState(query = searchMemory.query))
    val state: StateFlow<SearchUiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            modelManager.downloadProgress.collect { progress ->
                uiState.update { it.copy(downloadProgress = progress) }
            }
        }
        if (searchMemory.query.isNotBlank()) {
            search(searchMemory.query)
        }
    }

    fun onQueryChange(query: String) {
        uiState.update { it.copy(query = query) }
    }

    fun search(query: String = uiState.value.query) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            uiState.update { it.copy(query = trimmed, loading = true, error = null, needsModel = false) }
            if (!modelManager.checkIfModelAvailable()) {
                uiState.update { it.copy(loading = false, needsModel = true, results = emptyList(), intent = null) }
                return@launch
            }
            runCatching { searchProducts(trimmed) }
                .onSuccess { outcome ->
                    searchMemory.query = trimmed
                    searchMemory.intent = outcome.intent
                    searchMemory.results = outcome.results
                    uiState.update {
                        it.copy(
                            loading = false,
                            intent = outcome.intent,
                            results = outcome.results,
                            needsModel = false,
                        )
                    }
                }
                .onFailure { error ->
                    uiState.update {
                        it.copy(loading = false, error = error.message ?: "Search failed.")
                    }
                }
        }
    }

    fun downloadModel() {
        viewModelScope.launch {
            uiState.update { it.copy(error = null) }
            runCatching { modelManager.downloadModel() }
                .onSuccess { search(uiState.value.query) }
                .onFailure { error ->
                    uiState.update { it.copy(error = error.message ?: "Could not download the model.") }
                }
        }
    }
}
