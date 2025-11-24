package com.cristiancogollo.digitalstreaming

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClientsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ClientsHeader()
            Spacer(modifier = Modifier.height(20.dp))
            SearchBar()
            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(6) {
                    ClientCardItem()
                }
            }
            // --- AQUI IMPLEMENTAMOS EL COMPONENTE REUTILIZABLE ---
            SharedBottomNavigation(
                onNotificationClick = { /* Acción notificaciones */ },
                onHomeClick = { /* Navegar al Home */ },
                onStatsClick = { /* Acción Estadísticas */ }
            )
        }

        // Botón Flotante
        FloatingAddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
        )
    }
}

@Composable
fun ClientsHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // CAMBIO: Usamos Icons.Filled.People (el grupo de 3 personas)
        Icon(
            imageVector = Icons.Filled.People,
            contentDescription = "Clientes",
            tint = TextWhite,
            modifier = Modifier.size(45.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "CLIENTES",
            color = TextWhite,
            fontSize = 34.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(50.dp)
            .border(1.dp, BrandYellow, RoundedCornerShape(25.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Buscar",
            tint = BrandYellow,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Buscar Cliente...",
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ClientCardItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, BrandYellow)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Juan Perez",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "#Telefono",
                    color = BrandYellow,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // CAMBIO: Usamos Icons.Outlined.Info para que sea solo el contorno
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Info",
                tint = BrandYellow,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun FloatingAddButton(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.size(85.dp),
        shape = CircleShape,
        color = BrandYellow,
        shadowElevation = 8.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Agregar",
                tint = Color.Black,
                modifier = Modifier.size(45.dp)
            )
            Text(
                text = "Agregar",
                color = Color.Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewClientsScreen() {
    ClientsScreen()
}