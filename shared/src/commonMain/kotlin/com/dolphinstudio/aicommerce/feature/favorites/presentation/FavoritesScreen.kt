package com.dolphinstudio.aicommerce.feature.favorites.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dolphinstudio.aicommerce.core.designsystem.CommerceIconButton
import com.dolphinstudio.aicommerce.core.designsystem.EmptyState
import com.dolphinstudio.aicommerce.core.designsystem.Navy
import com.dolphinstudio.aicommerce.core.designsystem.ProductGrid
import com.dolphinstudio.aicommerce.feature.product.domain.Product
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoritesScreen(
    onBack: () -> Unit,
    onProduct: (Product) -> Unit,
    viewModel: FavoritesViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CommerceIconButton("←", "Back", onBack)
                Text("Favorites", style = MaterialTheme.typography.headlineSmall)
            }
        }
        item {
            if (state.products.isEmpty()) {
                EmptyState("No favorites yet", "Save a product and it will stay on this device.")
            } else {
                ProductGrid(products = state.products, onProductClick = onProduct)
            }
        }
    }
}
