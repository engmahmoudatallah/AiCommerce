package com.dolphinstudio.aicommerce.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dolphinstudio.aicommerce.core.designsystem.AIQueryField
import com.dolphinstudio.aicommerce.core.designsystem.CategoryChip
import com.dolphinstudio.aicommerce.core.designsystem.CommerceIconButton
import com.dolphinstudio.aicommerce.core.designsystem.Navy
import com.dolphinstudio.aicommerce.core.designsystem.OffWhite
import com.dolphinstudio.aicommerce.core.designsystem.ProductGrid
import com.dolphinstudio.aicommerce.core.designsystem.Sage
import com.dolphinstudio.aicommerce.core.designsystem.Turquoise
import com.dolphinstudio.aicommerce.feature.product.domain.Product
import org.koin.compose.viewmodel.koinViewModel

private val SuggestedPrompts = listOf(
    "Comfortable headphones for meetings",
    "Running shoes under 3000 EGP",
    "Lightweight laptop for programming",
)

private val Categories = listOf("Headphones", "Shoes", "Laptops", "Fashion", "Accessories")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onSearch: (String) -> Unit,
    onProduct: (Product) -> Unit,
    onFavorites: () -> Unit,
    onCart: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var query by rememberSaveable { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("AI Commerce", style = MaterialTheme.typography.displaySmall)
                    Text("A more personal way to shop.", style = MaterialTheme.typography.bodyMedium)
                }
                CommerceIconButton("♡", "Favorites", onFavorites)
                CommerceIconButton("Bag", "Cart", onCart)
            }
        }
        item {
            AIQueryField(
                value = query,
                onValueChange = { query = it },
                onSearch = { if (query.isNotBlank()) onSearch(query) },
            )
        }
        item {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SuggestedPrompts.forEach { prompt ->
                    CategoryChip(label = prompt, selected = false, onClick = { onSearch(prompt) })
                }
            }
        }
        item { ModelCard(state, viewModel::downloadModel) }
        item {
            Text("Categories", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(10.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Categories.forEach { category ->
                    CategoryChip(label = category, selected = false, onClick = { onSearch(category) })
                }
            }
        }
        item {
            Text("Recommended", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))
            ProductGrid(products = state.products, onProductClick = onProduct)
        }
    }
}

@Composable
private fun ModelCard(state: HomeUiState, onDownload: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Sage)
            .padding(18.dp),
    ) {
        Text("On-device search", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(6.dp))
        Text(
            text = if (state.modelReady) {
                "EmbeddingGemma is ready on this device."
            } else {
                "Download EmbeddingGemma 2 Text 270M to rank products locally."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = Navy,
        )
        state.modelError?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = Turquoise, style = MaterialTheme.typography.bodyMedium)
        }
        val progress = state.downloadProgress
        if (progress != null) {
            Spacer(Modifier.height(12.dp))
            if (progress < 0f) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Turquoise)
            } else {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = Turquoise,
                )
            }
        } else if (!state.modelReady) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Download model",
                color = OffWhite,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(Navy)
                    .clickable(onClick = onDownload)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
            )
        }
    }
}
