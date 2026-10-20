package com.dolphinstudio.aicommerce.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolphinstudio.aicommerce.core.ai.ModelManager
import com.dolphinstudio.aicommerce.feature.product.domain.GetProductsUseCase
import com.dolphinstudio.aicommerce.feature.product.domain.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val products: List<Product> = emptyList(),
    val modelReady: Boolean = false,
    val downloadProgress: Float? = null,
    val modelError: String? = null,
)

class HomeViewModel(
    getProducts: GetProductsUseCase,
    private val modelManager: ModelManager,
) : ViewModel() {
    private val modelReady = MutableStateFlow(false)
    private val modelError = MutableStateFlow<String?>(null)

    val state: StateFlow<HomeUiState> = combine(
        getProducts.recommended(),
        modelReady,
        modelManager.downloadProgress,
        modelError,
    ) { products, ready, progress, error ->
        HomeUiState(
            products = products.sortedByDescending { it.rating }.take(8),
            modelReady = ready,
            downloadProgress = progress,
            modelError = error,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    init {
        refreshModel()
    }

    fun refreshModel() {
        viewModelScope.launch {
            modelReady.value = modelManager.checkIfModelAvailable()
        }
    }

    fun downloadModel() {
        viewModelScope.launch {
            modelError.value = null
            runCatching { modelManager.downloadModel() }
                .onSuccess { modelReady.value = modelManager.checkIfModelAvailable() }
                .onFailure { modelError.value = it.message ?: "Could not download the model." }
        }
    }
}
