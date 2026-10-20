package com.dolphinstudio.aicommerce.feature.search.presentation

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dolphinstudio.aicommerce.core.common.formatPrice
import com.dolphinstudio.aicommerce.core.designsystem.AIQueryField
import com.dolphinstudio.aicommerce.core.designsystem.CardWhite
import com.dolphinstudio.aicommerce.core.designsystem.CategoryChip
import com.dolphinstudio.aicommerce.core.designsystem.CommerceIconButton
import com.dolphinstudio.aicommerce.core.designsystem.EmptyState
import com.dolphinstudio.aicommerce.core.designsystem.LoadingState
import com.dolphinstudio.aicommerce.core.designsystem.MatchBadge
import com.dolphinstudio.aicommerce.core.designsystem.Navy
import com.dolphinstudio.aicommerce.core.designsystem.OffWhite
import com.dolphinstudio.aicommerce.core.designsystem.PriceText
import com.dolphinstudio.aicommerce.core.designsystem.ProductVisual
import com.dolphinstudio.aicommerce.core.designsystem.RatingView
import com.dolphinstudio.aicommerce.core.designsystem.ReasonLine
import com.dolphinstudio.aicommerce.core.designsystem.Sage
import com.dolphinstudio.aicommerce.core.designsystem.Turquoise
import com.dolphinstudio.aicommerce.feature.search.domain.SearchIntent
import com.dolphinstudio.aicommerce.feature.search.domain.SearchResult
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AISearchScreen(
    onBack: () -> Unit,
    onProduct: (String) -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CommerceIconButton("←", "Back", onBack)
                Text("Search", style = MaterialTheme.typography.headlineSmall)
            }
        }
        item {
            AIQueryField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                onSearch = { viewModel.search() },
            )
        }
        state.intent?.let { intent ->
            item { IntentSummary(intent) }
        }
        when {
            state.loading -> item { LoadingState("Ranking products on this device") }
            state.needsModel -> item { ModelNeeded(state.downloadProgress, state.error, viewModel::downloadModel) }
            state.error != null -> item { EmptyState("Search paused", state.error ?: "") }
            state.intent != null && state.results.isEmpty() -> item {
                EmptyState("No close matches", "Try a wider budget or fewer filters.")
            }
            else -> items(state.results, key = { it.product.id }) { result ->
                SearchResultCard(result, onClick = { onProduct(result.product.id) })
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IntentSummary(intent: SearchIntent) {
    val chips = buildList {
        intent.category?.let(::add)
        intent.maxPrice?.let { add("Under ${formatPrice(it, intent.currency ?: "")}".trim()) }
        addAll(intent.brands)
        addAll(intent.colors)
        addAll(intent.attributes)
        intent.useCase?.let(::add)
    }.distinctBy { it.lowercase() }
    if (chips.isEmpty()) return
    Column {
        Text("Understood", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            chips.forEach { chip -> CategoryChip(label = chip, selected = true, onClick = {}) }
        }
    }
}

@Composable
private fun ModelNeeded(progress: Float?, error: String?, onDownload: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Sage)
            .padding(20.dp),
    ) {
        Text("On-device model needed", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(6.dp))
        Text(
            "Search uses EmbeddingGemma locally. Download it once, then this query can be ranked offline.",
            style = MaterialTheme.typography.bodyMedium,
            color = Navy,
        )
        if (error != null) {
            Spacer(Modifier.height(8.dp))
            Text(error, color = Turquoise, style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(Modifier.height(12.dp))
        if (progress != null) {
            if (progress < 0f) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Turquoise)
            } else {
                LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth(), color = Turquoise)
            }
        } else {
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

@Composable
private fun SearchResultCard(result: SearchResult, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .clickable(onClick = onClick)
            .padding(14.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ProductVisual(result.product.imageUrl, result.product.category, Modifier.height(92.dp).fillMaxWidth(0.28f))
            Column(Modifier.weight(1f)) {
                Text(result.product.name, style = MaterialTheme.typography.titleLarge)
                PriceText(result.product.price, result.product.currency)
                RatingView(result.product.rating)
                Spacer(Modifier.height(6.dp))
                MatchBadge(result.matchPercentage)
            }
        }
        Spacer(Modifier.height(12.dp))
        Text("Why this matches", style = MaterialTheme.typography.titleMedium)
        result.reasons.forEach { ReasonLine(it) }
    }
}
