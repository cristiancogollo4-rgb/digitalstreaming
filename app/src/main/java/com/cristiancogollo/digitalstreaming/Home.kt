package com.cristiancogollo.digitalstreaming

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Recibimos el NavController para poder navegar al hacer click en las cartas
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeaderSection()
        Spacer(modifier = Modifier.height(40.dp))

        // Pasamos el navController a la grilla
        MenuGridSection(navController)

        // NOTA: Eliminamos SharedBottomNavigation de aquí porque ya está en MainActivity
    }
}

@Composable
fun MenuGridSection(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        // Fila 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            MenuCard(
                title = "Clientes",
                icon = Icons.Default.Person,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(AppScreens.Clientes.route) } // NAVEGACIÓN
            )
            MenuCard(
                title = "Productos",
                icon = Icons.Outlined.PlayCircle,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(AppScreens.Productos.route) } // NAVEGACIÓN
            )
        }

        // Fila 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            MenuCard(
                title = "Ventas",
                icon = Icons.Default.ConfirmationNumber,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(AppScreens.Ventas.route) } // NAVEGACIÓN
            )
            MenuCard(
                title = "Programar\nMensaje",
                icon = Icons.Default.Chat,
                modifier = Modifier.weight(1f),
                onClick = { /* Acción futura */ }
            )
        }
    }
}

@Composable
fun MenuCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit // Añadimos evento Click
) {
    Card(
        modifier = modifier
            .aspectRatio(0.85f)
            .clickable { onClick() }, // Hacemos la carta clickeable
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.5.dp, BrandYellow)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = BrandYellow,
                modifier = Modifier.size(55.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun HeaderSection() {
    // (Tu código de Header original, sin cambios)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Movie,
            contentDescription = "Logo",
            tint = TextWhite,
            modifier = Modifier.size(60.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = "Digital", color = TextWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "STREAMING", color = BrandYellow, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
        }
    }
}