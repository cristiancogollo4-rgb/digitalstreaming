package com.cristiancogollo.digitalstreaming


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ConfirmationNumber // Icono para el ticket
import androidx.compose.material.icons.outlined.ShoppingCart // Icono para el carrito del header
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Modelo de datos para una Venta ---
data class SaleItem(
    val id: Int,
    val customerName: String,
    val detailSubtitle: String, // Puede ser fecha (DD/MM/AA) o "PRODUCTO"
    val price: String
)

// Datos de ejemplo basados en el mockup
val sampleSales = listOf(
    SaleItem(1, "Juan Perez", "DD/MM/AA", "$13.000"),
    SaleItem(2, "Juan Perez", "PRODUCTO", "$13.000"),
    SaleItem(3, "Juan Perez", "DD/MM/AA", "$13.000"),
    SaleItem(4, "Juan Perez", "PRODUCTO", "$13.000"),
    SaleItem(5, "Juan Perez", "DD/MM/AA", "$13.000"),
    SaleItem(6, "Carlos Ruiz", "PRODUCTO", "$25.000") // Un extra para probar el scroll
)

@Composable
fun SalesScreen() {
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

            // 1. Header "VENTAS" con icono de carrito
            SalesHeader()

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Barra de Buscador (Adaptada para ventas)
            SearchBarSales()

            Spacer(modifier = Modifier.height(20.dp))

            // 3. Lista Vertical de Ventas (LazyColumn)
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Ocupa el espacio restante
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp), // Espacio entre tarjetas
                contentPadding = PaddingValues(bottom = 100.dp) // Espacio inferior para que el FAB no tape el último elemento
            ) {
                items(sampleSales) { sale ->
                    SalesCard(sale = sale)
                }
            }
        }

        // 5. Botón Flotante "Agregar" (Reutilizado)
        FloatingAddButtonSales(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
        )
    }
}

// --- Componentes Específicos de esta Pantalla ---

@Composable
fun SalesHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.ShoppingCart, // Icono de carrito
            contentDescription = "Ventas Icono",
            tint = TextWhite,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "VENTAS",
            color = TextWhite,
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp // Un poco de espaciado entre letras como en la imagen
        )
    }
}

@Composable
fun SearchBarSales() {
    // Misma estructura que los buscadores anteriores
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
            text = "Buscar Venta...", // Texto placeholder específico
            color = Color.Gray,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SalesCard(sale: SaleItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp), // Altura fija aproximada según el mockup
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent), // Fondo transparente
        border = BorderStroke(1.dp, BrandYellow) // Borde amarillo
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Separa contenido a los extremos
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Parte Izquierda: Nombre y Subtítulo
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = sale.customerName,
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = sale.detailSubtitle, // Ej: "DD/MM/AA" o "PRODUCTO"
                    color = BrandYellow, // Color amarillo para el subtítulo
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Parte Derecha: Precio e Icono de Ticket
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = sale.price,
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Usamos un icono de ticket genérico (ConfirmationNumber)
                Icon(
                    imageVector = Icons.Outlined.ConfirmationNumber,
                    contentDescription = "Ticket",
                    tint = BrandYellow,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun FloatingAddButtonSales(modifier: Modifier = Modifier) {
    // El mismo botón que usamos en las otras pantallas
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
                contentDescription = "Agregar Venta",
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

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun PreviewSalesScreen() {
    // Asegúrate de que los colores estén disponibles en el preview si no son globales
    SalesScreen()
}