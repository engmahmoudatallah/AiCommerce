package com.dolphinstudio.aicommerce.feature.cart.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.dolphinstudio.aicommerce.core.common.formatPrice
import com.dolphinstudio.aicommerce.core.designsystem.CardWhite
import com.dolphinstudio.aicommerce.core.designsystem.CommerceIconButton
import com.dolphinstudio.aicommerce.core.designsystem.EmptyState
import com.dolphinstudio.aicommerce.core.designsystem.Navy
import com.dolphinstudio.aicommerce.core.designsystem.PriceText
import com.dolphinstudio.aicommerce.core.designsystem.Sage
import com.dolphinstudio.aicommerce.feature.cart.domain.CartLine
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CartScreen(
    onBack: () -> Unit,
    viewModel: CartViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val totals = state.lines.groupBy { it.product.currency }
        .mapValues { (_, lines) -> lines.sumOf { it.lineTotal } }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CommerceIconButton("←", "Back", onBack)
                Text("Cart", style = MaterialTheme.typography.headlineSmall)
            }
        }
        if (state.lines.isEmpty()) {
            item { EmptyState("Your cart is empty", "Add a product and it will stay here offline.") }
        } else {
            items(state.lines, key = { it.product.id }) { line ->
                CartRow(line, onChange = { quantity -> viewModel.changeQuantity(line.product.id, quantity) })
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(Sage)
                        .padding(16.dp),
                ) {
                    totals.forEach { (currency, total) ->
                        Text("Total ${formatPrice(total, currency)}", style = MaterialTheme.typography.titleLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun CartRow(line: CartLine, onChange: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(CardWhite)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(line.product.name, style = MaterialTheme.typography.titleMedium)
            PriceText(line.lineTotal, line.product.currency)
        }
        QuantityButton("−", onClick = { onChange(line.quantity - 1) })
        Text("${line.quantity}", modifier = Modifier.padding(horizontal = 10.dp), fontWeight = FontWeight.SemiBold)
        QuantityButton("+", onClick = { onChange(line.quantity + 1) })
    }
}

@Composable
private fun QuantityButton(label: String, onClick: () -> Unit) {
    Text(
        text = label,
        color = Navy,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Sage)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}
