package com.cristiancogollo.digitalstreaming

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
// --- UTILIDADES DE FECHA ---
import java.util.concurrent.TimeUnit
import java.util.Date
@Composable
fun SalesScreen(
    navController: NavController,
    viewModel: SalesViewModel = viewModel() // Inyectamos el ViewModel
) {
    val sales = viewModel.filteredSales
    val isLoading = viewModel.isLoading

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

            SalesHeader()

            Spacer(modifier = Modifier.height(20.dp))

            // Buscador (Reutilizamos lógica visual)
            SearchBarSales(
                query = viewModel.searchQuery,
                onQueryChange = { viewModel.searchQuery = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // LISTA REAL
            if (isLoading) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BrandYellow)
                }
            } else if (sales.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("No hay ventas registradas", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(sales) { sale ->
                        SalesCard(sale = sale)
                    }
                }
            }
        }

        FloatingAddButtonSales(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp),
            onClick = { navController.navigate(AppScreens.AddSale.route) }
        )
    }
}

// --- Componentes ---

@Composable
fun SalesHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.ShoppingCart,
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
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun SearchBarSales(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Buscar venta...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Filled.Search, null, tint = BrandYellow) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite,
            cursorColor = BrandYellow,
            focusedIndicatorColor = BrandYellow,
            unfocusedIndicatorColor = BrandYellow
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}

@Composable
fun SalesCard(sale: SaleModel) {
    // Formateadores
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    currencyFormat.maximumFractionDigits = 0
    val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())

    // 1. OBTENER ESTADO Y COLOR
    val statusColor = getStatusColor(sale.expiryDate)
    val statusText = getStatusText(sale.expiryDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp), // Un pelín más alto
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), // Fondo gris oscuro
        // 2. BORDE DINÁMICO (Semáforo)
        border = BorderStroke(1.5.dp, statusColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // IZQUIERDA: Info Cliente y Producto
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = sale.clientName,
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = sale.productName,
                    color = Color.Gray, // Producto en gris suave
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                // FECHA Y ESTADO
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = statusColor, // Icono del color del estado
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = statusText, // Ej: "Vence MAÑANA"
                        color = statusColor, // Texto del color del estado
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // DERECHA: Precio
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = currencyFormat.format(sale.salePrice),
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                // Nombre del Proveedor pequeño (para que sepas de dónde salió)
                Text(
                    text = sale.providerName,
                    color = Color.DarkGray,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun FloatingAddButtonSales(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .size(85.dp)
            .clickable { onClick() },
        shape = CircleShape,
        color = BrandYellow,
        shadowElevation = 8.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Filled.Add, "Agregar", tint = Color.Black, modifier = Modifier.size(45.dp))
            Text("Agregar", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
        }
    }
}
fun getStatusColor(expiryDate: Date): Color {
    val diff = expiryDate.time - Date().time
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        days < 0 -> Color(0xFFFF5252) // Rojo (Vencida)
        days <= 3 -> BrandYellow      // Amarillo (Por vencer)
        else -> Color(0xFF4CAF50)     // Verde (Activa)
    }
}

// Devuelve un texto corto de estado
fun getStatusText(expiryDate: Date): String {
    val diff = expiryDate.time - Date().time
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return when {
        days < 0 -> "VENCIDA hace ${-days} días"
        days == 0L -> "VENCE HOY"
        days == 1L -> "Vence MAÑANA"
        else -> "Faltan $days días"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSalesScreen() {
    val navController = rememberNavController()
    SalesScreen(navController = navController)
}