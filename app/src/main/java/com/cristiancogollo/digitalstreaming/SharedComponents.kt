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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Asegúrate de que los colores sean accesibles aquí
val SharedBrandYellow = Color(0xFFFFE066)
val SharedTextWhite = Color(0xFFEEEEEE)

@Composable
fun SharedBottomNavigation(
    onNotificationClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onStatsClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF282828)) // Fondo oscuro
    ) {
        // Línea divisoria
        Divider(color = Color.Gray, thickness = 0.5.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Distribuye los botones equitativamente
            verticalAlignment = Alignment.Top
        ) {
            SharedBottomNavItem(
                icon = Icons.Default.Notifications,
                label = "Notificaciones",
                onClick = onNotificationClick
            )
            SharedBottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                onClick = onHomeClick
            )
            SharedBottomNavItem(
                icon = Icons.Default.BarChart,
                label = "Estadísticas",
                onClick = onStatsClick
            )
        }
    }
}

@Composable
fun SharedBottomNavItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() } // Hace que todo el item sea clickeable
            .padding(4.dp) // Un poco de área touch extra
    ) {
        // Círculo alrededor del icono
        Box(
            modifier = Modifier
                .size(45.dp)
                .border(1.5.dp, SharedBrandYellow, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = SharedTextWhite,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = SharedTextWhite,
            fontSize = 11.sp, // Tamaño ajustado para uniformidad
            fontWeight = FontWeight.Bold
        )
    }
}