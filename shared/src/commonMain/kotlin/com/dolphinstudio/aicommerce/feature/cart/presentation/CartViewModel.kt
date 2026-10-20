package com.dolphinstudio.aicommerce.feature.cart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dolphinstudio.aicommerce.feature.cart.domain.CartLine
import com.dolphinstudio.aicommerce.feature.cart.domain.GetCartUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CartUiState(
    val lines: List<CartLine> = emptyList(),
)

class CartViewModel(
    private val getCart: GetCartUseCase,
) : ViewModel() {
    val state: StateFlow<CartUiState> = getCart()
        .map { CartUiState(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CartUiState())

    fun changeQuantity(productId: String, quantity: Int) {
        viewModelScope.launch { getCart.updateQuantity(productId, quantity) }
    }
}
