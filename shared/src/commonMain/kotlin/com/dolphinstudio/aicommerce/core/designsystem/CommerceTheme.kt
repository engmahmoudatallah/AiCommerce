package com.dolphinstudio.aicommerce.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val OffWhite = Color(0xFFF4F1EA)
val Navy = Color(0xFF122033)
val InkMuted = Color(0xFF5C6B7A)
val Sage = Color(0xFFE3EFE4)
val SageDeep = Color(0xFFD3E5D6)
val Turquoise = Color(0xFF1C9B96)
val CardWhite = Color(0xFFFFFCF8)
val Line = Color(0xFFE4DDD2)

private val Colors = lightColorScheme(
    primary = Navy,
    onPrimary = OffWhite,
    primaryContainer = Sage,
    onPrimaryContainer = Navy,
    secondary = Turquoise,
    onSecondary = Color.White,
    secondaryContainer = SageDeep,
    onSecondaryContainer = Navy,
    background = OffWhite,
    onBackground = Navy,
    surface = CardWhite,
    onSurface = Navy,
    surfaceVariant = Sage,
    onSurfaceVariant = InkMuted,
    outline = Line,
)

private val Type = Typography(
    displaySmall = TextStyle(fontSize = 40.sp, lineHeight = 44.sp, fontWeight = FontWeight.SemiBold, color = Navy),
    headlineMedium = TextStyle(fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.SemiBold, color = Navy),
    headlineSmall = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold, color = Navy),
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold, color = Navy),
    titleMedium = TextStyle(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.Medium, color = Navy),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal, color = Navy),
    bodyMedium = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Normal, color = InkMuted),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 18.sp, fontWeight = FontWeight.SemiBold, color = Navy),
)

@Composable
fun CommerceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Colors,
        typography = Type,
        content = content,
    )
}
