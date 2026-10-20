package com.dolphinstudio.aicommerce.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dolphinstudio.aicommerce.core.designsystem.Navy
import com.dolphinstudio.aicommerce.core.designsystem.OffWhite
import com.dolphinstudio.aicommerce.core.designsystem.Sage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashScreen(
    onFinished: () -> Unit,
    viewModel: SplashViewModel = koinViewModel(),
) {
    val ready by viewModel.ready.collectAsStateWithLifecycle()
    LaunchedEffect(ready) {
        if (ready) onFinished()
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Navy),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "AI Commerce",
                color = OffWhite,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(8.dp))
            Text("Find it in your own words.", color = Sage, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
