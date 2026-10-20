package com.dolphinstudio.aicommerce.di

import com.dolphinstudio.aicommerce.core.database.AppDatabase
import com.dolphinstudio.aicommerce.feature.cart.data.CartRepositoryImpl
import com.dolphinstudio.aicommerce.feature.cart.domain.AddToCartUseCase
import com.dolphinstudio.aicommerce.feature.cart.domain.CartRepository
import com.dolphinstudio.aicommerce.feature.cart.domain.GetCartUseCase
import com.dolphinstudio.aicommerce.feature.cart.presentation.CartViewModel
import com.dolphinstudio.aicommerce.feature.favorites.data.FavoriteRepositoryImpl
import com.dolphinstudio.aicommerce.feature.favorites.domain.FavoriteRepository
import com.dolphinstudio.aicommerce.feature.favorites.domain.GetFavoritesUseCase
import com.dolphinstudio.aicommerce.feature.favorites.domain.ToggleFavoriteUseCase
import com.dolphinstudio.aicommerce.feature.favorites.presentation.FavoritesViewModel
import com.dolphinstudio.aicommerce.feature.home.presentation.HomeViewModel
import com.dolphinstudio.aicommerce.feature.home.presentation.SplashViewModel
import com.dolphinstudio.aicommerce.feature.product.data.ProductRepositoryImpl
import com.dolphinstudio.aicommerce.feature.product.domain.GetProductDetailsUseCase
import com.dolphinstudio.aicommerce.feature.product.domain.GetProductsUseCase
import com.dolphinstudio.aicommerce.feature.product.domain.ProductRepository
import com.dolphinstudio.aicommerce.feature.product.domain.SeedCatalogUseCase
import com.dolphinstudio.aicommerce.feature.product.presentation.ProductDetailsViewModel
import com.dolphinstudio.aicommerce.feature.search.domain.IntentParser
import com.dolphinstudio.aicommerce.feature.search.domain.MatchReasonGenerator
import com.dolphinstudio.aicommerce.feature.search.domain.ProductRanker
import com.dolphinstudio.aicommerce.feature.search.domain.SearchProductsUseCase
import com.dolphinstudio.aicommerce.feature.search.domain.SemanticSearchEngine
import com.dolphinstudio.aicommerce.feature.search.presentation.SearchMemory
import com.dolphinstudio.aicommerce.feature.search.presentation.SearchViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

fun initKoin(platformModule: Module) {
    if (KoinPlatform.getKoinOrNull() != null) return
    startKoin {
        modules(sharedModule, platformModule)
    }
}

private val sharedModule = module {
    single { get<AppDatabase>().productDao() }
    single { get<AppDatabase>().favoriteDao() }
    single { get<AppDatabase>().cartDao() }

    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single<FavoriteRepository> { FavoriteRepositoryImpl(get(), get()) }
    single<CartRepository> { CartRepositoryImpl(get(), get()) }

    single { IntentParser() }
    single { ProductRanker() }
    single { MatchReasonGenerator() }
    single { SemanticSearchEngine(get(), get(), get(), get()) }
    single { SearchMemory() }

    factory { SearchProductsUseCase(get(), get()) }
    factory { GetProductsUseCase(get()) }
    factory { GetProductDetailsUseCase(get()) }
    factory { SeedCatalogUseCase(get()) }
    factory { ToggleFavoriteUseCase(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { AddToCartUseCase(get()) }
    factory { GetCartUseCase(get()) }

    viewModel { SplashViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { (productId: String) ->
        ProductDetailsViewModel(productId, get(), get(), get(), get(), get())
    }
    viewModel { FavoritesViewModel(get()) }
    viewModel { CartViewModel(get()) }
}
