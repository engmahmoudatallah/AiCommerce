package com.dolphinstudio.aicommerce.di

import com.dolphinstudio.aicommerce.core.ai.EmbeddingEngine
import com.dolphinstudio.aicommerce.core.ai.IosLiteRtEmbeddingEngine
import com.dolphinstudio.aicommerce.core.ai.IosModelManager
import com.dolphinstudio.aicommerce.core.ai.ModelManager
import com.dolphinstudio.aicommerce.core.database.AppDatabase
import com.dolphinstudio.aicommerce.core.database.getDatabaseBuilder
import org.koin.dsl.module

fun iosPlatformModule() = module {
    single<AppDatabase> { getDatabaseBuilder().build() }
    single<ModelManager> { IosModelManager() }
    single<EmbeddingEngine> { IosLiteRtEmbeddingEngine(get()) }
}
