package com.dolphinstudio.aicommerce.feature.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolphinstudio.aicommerce.feature.cart.domain.AddToCartUseCase
import com.dolphinstudio.aicommerce.feature.favorites.domain.GetFavoritesUseCase
import com.dolphinstudio.aicommerce.feature.favorites.domain.ToggleFavoriteUseCase
import com.dolphinstudio.aicommerce.feature.product.domain.GetProductDetailsUseCase
import com.dolphinstudio.aicommerce.feature.product.domain.Product
import com.dolphinstudio.aicommerce.feature.search.domain.SearchResult
import com.dolphinstudio.aicommerce.feature.search.presentation.SearchMemory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductDetailsUiState(
    val product: Product? = null,
    val favorite: Boolean = false,
    val match: SearchResult? = null,
    val addedToCart: Boolean = false,
    val loading: Boolean = true,
)

class ProductDetailsViewModel(
    private val productId: String,
    private val getProductDetails: GetProductDetailsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    getFavorites: GetFavoritesUseCase,
    private val addToCart: AddToCartUseCase,
    searchMemory: SearchMemory,
) : ViewModel() {
    private val uiState = MutableStateFlow(
        ProductDetailsUiState(match = searchMemory.results.firstOrNull { it.product.id == productId }),
    )
    val state: StateFlow<ProductDetailsUiState> = uiState.asStateFlow()

    init {
        viewModelScope.launch {
            uiState.update { it.copy(product = getProductDetails(productId), loading = false) }
        }
        viewModelScope.launch {
            getFavorites.ids().collect { ids ->
                uiState.update { it.copy(favorite = productId in ids) }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch { toggleFavorite(productId) }
    }

    fun addToCart() {
        viewModelScope.launch {
            addToCart(productId)
            uiState.update { it.copy(addedToCart = true) }
        }
    }
}
