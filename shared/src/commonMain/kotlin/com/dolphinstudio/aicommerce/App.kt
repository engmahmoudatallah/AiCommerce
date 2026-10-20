package com.dolphinstudio.aicommerce

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dolphinstudio.aicommerce.core.designsystem.CommerceTheme
import com.dolphinstudio.aicommerce.core.designsystem.OffWhite
import com.dolphinstudio.aicommerce.feature.search.presentation.SearchMemory
import com.dolphinstudio.aicommerce.navigation.AppNav
import org.koin.compose.koinInject

@Composable
fun App() {
    val searchMemory = koinInject<SearchMemory>()
    CommerceTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(OffWhite)
                .safeDrawingPadding(),
        ) {
            AppNav(searchMemory)
        }
    }
}
