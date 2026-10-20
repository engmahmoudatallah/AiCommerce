package com.dolphinstudio.aicommerce.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dolphinstudio.aicommerce.feature.cart.presentation.CartScreen
import com.dolphinstudio.aicommerce.feature.favorites.presentation.FavoritesScreen
import com.dolphinstudio.aicommerce.feature.home.presentation.HomeScreen
import com.dolphinstudio.aicommerce.feature.home.presentation.SplashScreen
import com.dolphinstudio.aicommerce.feature.product.presentation.ProductDetailsScreen
import com.dolphinstudio.aicommerce.feature.search.presentation.AISearchScreen
import com.dolphinstudio.aicommerce.feature.search.presentation.SearchMemory

object Routes {
    const val Splash = "splash"
    const val Home = "home"
    const val Search = "search"
    const val Product = "product"
    const val Favorites = "favorites"
    const val Cart = "cart"
}

@Composable
fun AppNav(searchMemory: SearchMemory) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Splash,
        modifier = Modifier.fillMaxSize(),
    ) {
        composable(Routes.Splash) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.Home) {
                        popUpTo(Routes.Splash) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.Home) {
            HomeScreen(
                onSearch = { query ->
                    searchMemory.query = query
                    navController.navigate(Routes.Search)
                },
                onProduct = { product ->
                    searchMemory.selectedProductId = product.id
                    navController.navigate(Routes.Product)
                },
                onFavorites = { navController.navigate(Routes.Favorites) },
                onCart = { navController.navigate(Routes.Cart) },
            )
        }
        composable(Routes.Search) {
            AISearchScreen(
                onBack = { navController.popBackStack() },
                onProduct = { id ->
                    searchMemory.selectedProductId = id
                    navController.navigate(Routes.Product)
                },
            )
        }
        composable(Routes.Product) {
            ProductDetailsScreen(
                productId = searchMemory.selectedProductId,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.Favorites) {
            FavoritesScreen(
                onBack = { navController.popBackStack() },
                onProduct = { product ->
                    searchMemory.selectedProductId = product.id
                    navController.navigate(Routes.Product)
                },
            )
        }
        composable(Routes.Cart) {
            CartScreen(onBack = { navController.popBackStack() })
        }
    }
}
