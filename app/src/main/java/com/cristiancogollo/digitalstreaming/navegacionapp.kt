package com.cristiancogollo.digitalstreaming

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

sealed class AppScreens(val route: String) {
    object Home : AppScreens("home_screen")
    object Ventas : AppScreens("ventas_screen")
    object Productos : AppScreens("productos_screen")
    object Notificaciones : AppScreens("notifications_screen")
    object Clientes : AppScreens("clientes_screen")
    // Estadísticas no tiene pantalla aún, así que no necesita ruta por ahora
    object AddProduct : AppScreens("add_producto_screen")
    object ProductDetail : AppScreens("product_detail_screen/{productId}") {
        fun createRoute(productId: String) = "product_detail_screen/$productId"
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreens.Home.route
    ) {
        // HOME (Ahora pasamos el navController para que los botones del menú funcionen)
        composable(route = AppScreens.Home.route) {
            HomeScreen(navController)
        }

        // VENTAS
        composable(route = AppScreens.Ventas.route) {
            SalesScreen()
        }

        // PRODUCTOS
        composable(route = AppScreens.Productos.route) {
            ProductsScreen(navController) // Descomenta cuando tengas la pantalla real
        }

        // CLIENTES
        composable(route = AppScreens.Clientes.route) {
            ClientsScreen()
        }

        // NOTIFICACIONES
        composable(route = AppScreens.Notificaciones.route) {
            PlaceholderScreen("Pantalla Notificaciones")
        }
        //addproducto
        composable(route = AppScreens.AddProduct.route) {
            AddProductScreen(navController = navController)
        }
        composable(
            route = AppScreens.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Extraemos el ID que viene en la ruta
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(navController = navController, productId = productId)
        }
    }
}

// Pantalla temporal para evitar errores en Productos/Notificaciones
@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = name, color = Color.White)
    }
}