package com.dolphinstudio.aicommerce.core.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dolphinstudio.aicommerce.core.common.formatPrice
import com.dolphinstudio.aicommerce.feature.product.domain.Product

@Composable
fun AIQueryField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "What are you looking for?",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(CardWhite)
            .border(1.dp, Line, RoundedCornerShape(28.dp))
            .padding(start = 18.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            textStyle = MaterialTheme.typography.bodyLarge,
            cursorBrush = SolidColor(Turquoise),
            decorationBox = { inner ->
                Box {
                    if (value.isBlank()) {
                        Text(placeholder, style = MaterialTheme.typography.bodyMedium)
                    }
                    inner()
                }
            },
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Turquoise)
                .clickable(onClick = onSearch)
                .padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            Text("Search", color = CardWhite, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val background = if (selected) Navy else CardWhite
    val content = if (selected) OffWhite else Navy
    Text(
        text = label,
        color = content,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(background)
            .border(1.dp, if (selected) Navy else Line, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    )
}

@Composable
fun MatchBadge(percent: Int, modifier: Modifier = Modifier) {
    Text(
        text = "$percent% Match",
        color = Navy,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Sage)
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}

@Composable
fun PriceText(price: Double, currency: String, modifier: Modifier = Modifier) {
    Text(
        text = formatPrice(price, currency),
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium,
        color = Navy,
    )
}

@Composable
fun RatingView(rating: Double, modifier: Modifier = Modifier) {
    val filled = rating.toInt().coerceIn(0, 5)
    Text(
        text = "★".repeat(filled) + "☆".repeat(5 - filled),
        modifier = modifier,
        color = Turquoise,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
fun ProductVisual(imageKey: String, category: String, modifier: Modifier = Modifier) {
    val painter = productPainter(imageKey)
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(Sage),
        contentAlignment = Alignment.Center,
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = category,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Text(
                text = category.take(1),
                style = MaterialTheme.typography.headlineMedium,
                color = Navy,
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    matchPercentage: Int? = null,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(CardWhite)
            .clickable(onClick = onClick)
            .padding(12.dp),
    ) {
        ProductVisual(product.imageUrl, product.category, Modifier.fillMaxWidth().height(112.dp))
        Spacer(Modifier.height(10.dp))
        Text(product.name, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(4.dp))
        PriceText(product.price, product.currency)
        RatingView(product.rating)
        if (matchPercentage != null) {
            Spacer(Modifier.height(8.dp))
            MatchBadge(matchPercentage)
        }
    }
}

@Composable
fun ProductGrid(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier,
    matches: Map<String, Int> = emptyMap(),
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        products.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { product ->
                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product) },
                        matchPercentage = matches[product.id],
                        modifier = Modifier.weight(1f),
                    )
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun EmptyState(title: String, message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Sage)
            .padding(24.dp),
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(6.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun LoadingState(message: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(color = Turquoise, modifier = Modifier.size(28.dp))
        Spacer(Modifier.height(12.dp))
        Text(message, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun CommerceIconButton(
    symbol: String,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(symbol, color = Navy, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun ReasonLine(text: String) {
    Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
        Text("✓", color = Turquoise, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, color = Navy)
    }
}
