package com.dolphinstudio.aicommerce

import androidx.compose.ui.window.ComposeUIViewController
import com.dolphinstudio.aicommerce.di.initKoin
import com.dolphinstudio.aicommerce.di.iosPlatformModule

fun MainViewController() = ComposeUIViewController {
    initKoin(iosPlatformModule())
    App()
}