package com.cristiancogollo.digitalstreaming

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// --- Datos de ejemplo para la lista de productos ---
data class Product(val name: String, val price: String, val duration: String)

val sampleProducts = listOf(
    Product("Netflix", "$13.000", "30 Días"),
    Product("Disney", "$13.000", "30 Días"),
    Product("Hbo Max", "$13.000", "30 Días"),
    Product("Prime", "$13.000", "30 Días"),
    Product("Star+", "$13.000", "30 Días"),
    Product("Youtube Premium", "$13.000", "30 Días")
)

@Composable
fun ProductsScreen() {
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

            // 1. Header "PRODUCTOS"
            ProductsHeader()

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Barra de Buscador (Reutilizada de ClientsScreen)
            SearchBarProducts()

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Grid de Productos (Scrollable)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 columnas fijas
                modifier = Modifier
                    .weight(1f) // Ocupa todo el espacio restante
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp), // Espacio entre columnas
                verticalArrangement = Arrangement.spacedBy(16.dp),   // Espacio entre filas
                contentPadding = PaddingValues(bottom = 100.dp) // Espacio para el FAB
            ) {
                items(sampleProducts) { product ->
                    ProductCard(product)
                }
            }

            // 4. Barra de Navegación Inferior (Reutilizamos el componente SharedBottomNavigation)
            SharedBottomNavigation(
                onNotificationClick = { /* Acción Notificaciones */ },
                onHomeClick = { /* Acción Home */ },
                onStatsClick = { /* Acción Estadísticas */ }
            )
        }

        // 5. Botón Flotante "Agregar" (Reutilizado de ClientsScreen)
        FloatingAddButtonProducts(
            modifier = Modifier
                .align(Alignment.BottomEnd) // Alineado abajo a la derecha
                .padding(bottom = 90.dp, end = 20.dp) // Separación del borde y del menú
        )
    }
}

// --- Componentes específicos de esta pantalla o adaptados ---

@Composable
fun ProductsHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.PlayCircle, // Icono de Play, como en tu primer mockup
            contentDescription = "Productos",
            tint = TextWhite,
            modifier = Modifier.size(50.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "PRODUCTOS",
            color = TextWhite,
            fontSize = 34.sp,
            fontWeight = FontWeight.Black
        )
    }
}

@Composable
fun SearchBarProducts() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(50.dp)
            .border(1.dp, BrandYellow, RoundedCornerShape(25.dp)) // Borde redondeado amarillo
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
            text = "Buscar Produc...",
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f), // Ajusta la proporción para que sea ligeramente más alto que ancho
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, BrandYellow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // "30 Días" en la esquina superior derecha
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Text(
                    text = product.duration,
                    color = BrandYellow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // --- CÓDIGO PARA IMAGEN (DESCOMENTAR Y ADAPTAR SI SE NECESITA) ---
            /*
            // Recuerda añadir la dependencia Coil o Glide para cargar imágenes de URL
            // implementation("io.coil-kt:coil-compose:2.x.x")
            Image(
                painter = rememberImagePainter(product.imageUrl), // Necesitarías un campo imageUrl en tu Product data class
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp) // Tamaño de la imagen
                    .clip(RoundedCornerShape(8.dp)), // Bordes redondeados para la imagen
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            */
            // --- FIN CÓDIGO PARA IMAGEN ---

            // Texto "Netflix", "Disney", etc.
            Text(
                text = product.name,
                color = TextWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp // Espacio entre líneas si el nombre es largo
            )

            // Precio
            Text(
                text = product.price,
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun FloatingAddButtonProducts(modifier: Modifier = Modifier) {
    // Componente reutilizado del botón flotante "Agregar"
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


// --- Preview de la Pantalla ---
@Preview(showBackground = true)
@Composable
fun PreviewProductsScreen() {
    ProductsScreen()
}