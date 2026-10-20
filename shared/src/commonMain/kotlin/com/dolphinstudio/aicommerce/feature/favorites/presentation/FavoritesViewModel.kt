package com.dolphinstudio.aicommerce.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolphinstudio.aicommerce.feature.favorites.domain.GetFavoritesUseCase
import com.dolphinstudio.aicommerce.feature.product.domain.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class FavoritesUiState(
    val products: List<Product> = emptyList(),
)

class FavoritesViewModel(
    getFavorites: GetFavoritesUseCase,
) : ViewModel() {
    val state: StateFlow<FavoritesUiState> = getFavorites()
        .map { FavoritesUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FavoritesUiState())
}
