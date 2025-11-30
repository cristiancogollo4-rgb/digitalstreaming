package com.cristiancogollo.digitalstreaming

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

// Colores
val DarkBackground = Color(0xFF282828) // Gris oscuro casi negro
val BrandYellow = Color(0xFFFFE066)    // Amarillo corporativo
val TextWhite = Color(0xFFEEEEEE)      // Blanco suave para textos
@Composable
fun SharedBottomNavigation(
    navController: NavHostController
) {
    // Detectamos la ruta actual para saber qué icono pintar
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF282828))
    ) {
        Divider(color = Color.Gray, thickness = 0.5.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 24.dp), // Ajusté padding
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // 1. Notificaciones
            SharedBottomNavItem(
                icon = Icons.Default.Notifications,
                label = "Notificaciones",
                isSelected = currentRoute == AppScreens.Notificaciones.route,
                onClick = {
                    navController.navigate(AppScreens.Notificaciones.route) {
                        launchSingleTop = true
                    }
                }
            )

            // 2. Home
            SharedBottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = currentRoute == AppScreens.Home.route,
                onClick = {
                    navController.navigate(AppScreens.Home.route) {
                        popUpTo(AppScreens.Home.route) { inclusive = true }
                    }
                }
            )

            // 3. Estadísticas (Existe pero no navega)
            SharedBottomNavItem(
                icon = Icons.Default.BarChart,
                label = "Estadísticas",
                isSelected = false, // Nunca seleccionado por ahora
                onClick = {
                    // AQUÍ NO HACEMOS NADA POR AHORA
                }
            )
        }
    }
}

@Composable
fun SharedBottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean, // Nuevo parámetro para saber si pintarlo de amarillo
    onClick: () -> Unit
) {
    // Si está seleccionado usamos Amarillo, si no, Blanco
    val contentColor = if (isSelected) BrandYellow else TextWhite

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(45.dp)
                .border(
                    width = 1.5.dp,
                    color = contentColor, // Borde cambia de color
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor, // Icono cambia de color
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = contentColor, // Texto cambia de color
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}