package com.dolphinstudio.aicommerce.di

import android.content.Context
import com.dolphinstudio.aicommerce.core.ai.AndroidModelManager
import com.dolphinstudio.aicommerce.core.ai.EmbeddingEngine
import com.dolphinstudio.aicommerce.core.ai.LiteRtEmbeddingEngine
import com.dolphinstudio.aicommerce.core.ai.ModelManager
import com.dolphinstudio.aicommerce.core.database.AppDatabase
import com.dolphinstudio.aicommerce.core.database.getDatabaseBuilder
import org.koin.dsl.module

fun startAndroidApp(context: Context) {
    initKoin(androidPlatformModule(context))
}

fun androidPlatformModule(context: Context) = module {
    single { context.applicationContext }
    single<AppDatabase> { getDatabaseBuilder(get()).build() }
    single<ModelManager> { AndroidModelManager(get()) }
    single<EmbeddingEngine> { LiteRtEmbeddingEngine(get()) }
}
