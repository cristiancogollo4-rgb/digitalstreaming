package com.cristiancogollo.digitalstreaming

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductsScreen(
    navController: NavController,
    viewModel: ProductsViewModel = viewModel()
) {
    // --- CAMBIO: Usamos la lista FILTRADA del ViewModel ---
    val products = viewModel.filteredProducts
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

            ProductsHeader()

            Spacer(modifier = Modifier.height(20.dp))

            // --- CAMBIO: Pasamos el estado del buscador al componente ---
            SearchBarProducts(
                query = viewModel.searchQuery,
                onQueryChange = { viewModel.searchQuery = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BrandYellow)
                }
            } else if (products.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    // Mensaje diferente si no hay resultados en la búsqueda
                    val msg = if (viewModel.searchQuery.isNotEmpty()) "No se encontraron resultados" else "No hay productos guardados"
                    Text(msg, color = Color.Gray)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(products) { product ->
                        ProductCard(
                            product = product,
                            onClick = {
                                navController.navigate(AppScreens.ProductDetail.createRoute(product.id))
                            }
                        )
                    }
                }
            }
        }

        FloatingAddButtonProducts(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp),
            onClick = { navController.navigate(AppScreens.AddProduct.route) }
        )
    }
}

// --- HEADER ---
@Composable
fun ProductsHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.PlayCircle,
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

// --- BUSCADOR ACTUALIZADO (INTERACTIVO) ---
@Composable
fun SearchBarProducts(
    query: String,
    onQueryChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Buscar servicio...", color = Color.Gray, fontSize = 16.sp) },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = "Buscar", tint = BrandYellow, modifier = Modifier.size(28.dp))
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Limpiar", tint = Color.Gray)
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite,
            cursorColor = BrandYellow,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(55.dp) // Un poco más alto para que sea fácil de tocar
            .border(1.dp, BrandYellow, RoundedCornerShape(25.dp))
            .padding(start = 8.dp) // Ajuste interno
    )
}

// --- TARJETA DE PRODUCTO (CON IMAGEN) ---
@Composable
fun ProductCard(
    product: ProductModel,
    onClick: () -> Unit
) {
    // 1. Formatear Precio
    val formattedPrice = try {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
        format.maximumFractionDigits = 0
        format.format(product.salePrice)
    } catch (e: Exception) { "$ ${product.salePrice}" }

    // 2. Lógica Visual para Combos (Resaltar con Azul Neón)
    val isCombo = product.name.startsWith("Combo", ignoreCase = true)
    val borderColor = if (isCombo) Color(0xFF00E5FF) else BrandYellow
    val nameColor = if (isCombo) Color(0xFF00E5FF) else TextWhite

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f) // Un poco más alto para que quepa bien el nombre
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)), // Fondo gris oscuro base
        border = BorderStroke(1.5.dp, borderColor), // Borde dinámico
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            // --- A. IMAGEN DE FONDO ---
            if (product.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    error = rememberVectorPainter(Icons.Default.BrokenImage),
                    placeholder = rememberVectorPainter(Icons.Default.Image)
                )

                // --- B. CAPA DE OSCURECIMIENTO (Para leer el texto) ---
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)) // 60% de oscuridad
                )
            } else {
                // Si no hay imagen, un icono central sutil
                Icon(
                    imageVector = Icons.Outlined.PlayCircle,
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 0.3f),
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.Center)
                )
            }

            // --- C. CONTENIDO (TEXTO) ---
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Días (Esquina superior derecha)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                    Surface(
                        color = BrandYellow,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Text(
                            text = "${product.serviceDays} D",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                // Nombre del Producto (Centro)
                Text(
                    text = product.name,
                    color = nameColor, // Color dinámico (Blanco o Neón)
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    maxLines = 3,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = Color.Black, blurRadius = 10f
                        )
                    )
                )

                // Precio (Abajo)
                Text(
                    text = formattedPrice,
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = androidx.compose.ui.text.TextStyle(
                        shadow = androidx.compose.ui.graphics.Shadow(
                            color = Color.Black, blurRadius = 10f
                        )
                    )
                )
            }
        }
    }
}

// --- BOTÓN FLOTANTE ---
@Composable
fun FloatingAddButtonProducts(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.size(85.dp).clickable { onClick() },
        shape = CircleShape,
        color = BrandYellow,
        shadowElevation = 8.dp
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Color.Black, modifier = Modifier.size(45.dp))
            Text("Agregar", color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Black)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductsScreen() {
    val navController = rememberNavController()
    ProductsScreen(navController = navController)
}