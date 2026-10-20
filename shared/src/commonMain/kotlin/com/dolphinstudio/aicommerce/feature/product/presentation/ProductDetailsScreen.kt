package com.dolphinstudio.aicommerce.feature.product.presentation

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
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProductDetailsScreen(
    productId: String,
    onBack: () -> Unit,
    viewModel: ProductDetailsViewModel = koinViewModel { parametersOf(productId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val product = state.product
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            CommerceIconButton("←", "Back", onBack)
        }
        when {
            state.loading -> item { LoadingState("Loading product") }
            product == null -> item { EmptyState("Missing product", "This item is no longer in the catalog.") }
            else -> {
                item { ProductVisual(product.imageUrl, product.category, Modifier.fillMaxWidth().height(220.dp)) }
                item {
                    Text(product.name, style = MaterialTheme.typography.headlineMedium)
                    PriceText(product.price, product.currency)
                    RatingView(product.rating)
                }
                state.match?.let { match ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(Sage)
                                .padding(16.dp),
                        ) {
                            MatchBadge(match.matchPercentage)
                            Spacer(Modifier.height(8.dp))
                            Text("Why this matches", style = MaterialTheme.typography.titleMedium)
                            match.reasons.forEach { ReasonLine(it) }
                        }
                    }
                }
                item {
                    Text(product.description, style = MaterialTheme.typography.bodyLarge, color = Navy)
                }
                item {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        product.tags.forEach { tag ->
                            CategoryChip(label = tag, selected = false, onClick = {})
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (state.favorite) Sage else OffWhite)
                                .clickable(onClick = viewModel::toggleFavorite)
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = if (state.favorite) "♥ Saved" else "♡ Save",
                                color = if (state.favorite) Turquoise else Navy,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        Text(
                            text = if (state.addedToCart) "Added to cart" else "Add to cart",
                            color = OffWhite,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Navy)
                                .clickable(onClick = viewModel::addToCart)
                                .padding(horizontal = 18.dp, vertical = 12.dp),
                        )
                    }
                }
            }
        }
    }
}
