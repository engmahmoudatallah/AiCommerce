package com.dolphinstudio.aicommerce.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolphinstudio.aicommerce.feature.product.domain.SeedCatalogUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val seedCatalog: SeedCatalogUseCase,
) : ViewModel() {
    private val readyState = MutableStateFlow(false)
    val ready: StateFlow<Boolean> = readyState.asStateFlow()

    init {
        viewModelScope.launch {
            seedCatalog()
            readyState.value = true
        }
    }
}
