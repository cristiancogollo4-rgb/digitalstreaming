package com.cristiancogollo.digitalstreaming

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    viewModel: ProductDetailViewModel = viewModel()
) {
    // Estado para controlar si se muestra el diálogo
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        if (viewModel.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = BrandYellow)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. IMAGEN GRANDE
                Box(modifier = Modifier.height(250.dp).fillMaxWidth()) {
                    if (viewModel.imageUrl.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(viewModel.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopStart)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50))
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                }

                Column(modifier = Modifier.padding(24.dp)) {
                    Text(text = "EDITAR PRODUCTO", color = BrandYellow, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // 2. CAMPOS GENERALES
                    OutlinedTextField(
                        value = viewModel.name,
                        onValueChange = { viewModel.name = it },
                        label = { Text("Nombre") },
                        colors = fieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.salePrice,
                        onValueChange = { viewModel.salePrice = it },
                        label = { Text("Precio Venta ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = fieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = viewModel.serviceDays,
                        onValueChange = { viewModel.serviceDays = it },
                        label = { Text("Días de Servicio") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = fieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // 3. SECCIÓN PROVEEDORES
                    Text(text = "Costos por Proveedor", color = TextWhite, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Lista de proveedores existentes
                    viewModel.providers.forEach { (providerName, cost) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Botón pequeño para borrar proveedor (X roja)
                            IconButton(onClick = { viewModel.removeProvider(providerName) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red.copy(alpha = 0.6f))
                            }
                            Spacer(modifier = Modifier.width(8.dp))

                            Text(text = providerName, color = Color.Gray, modifier = Modifier.weight(1f))

                            OutlinedTextField(
                                value = cost,
                                onValueChange = { viewModel.providers[providerName] = it },
                                label = { Text("Costo") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors = fieldColors(),
                                modifier = Modifier.width(130.dp)
                            )
                        }
                    }

                    // --- NUEVO: BOTÓN AGREGAR PROVEEDOR ---
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = BrandYellow)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Agregar Proveedor", color = BrandYellow)
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    // 4. BOTONES GUARDAR / ELIMINAR
                    Button(
                        onClick = { viewModel.updateProduct(productId) { navController.popBackStack() } },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandYellow),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("GUARDAR CAMBIOS", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { viewModel.deleteProduct(productId) { navController.popBackStack() } },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ELIMINAR PRODUCTO")
                    }

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }

            // --- VENTANA EMERGENTE (DIALOG) ---
            if (showAddDialog) {
                AddProviderDialog(
                    onDismiss = { showAddDialog = false },
                    onConfirm = { name, cost ->
                        viewModel.addProvider(name, cost)
                        showAddDialog = false
                    }
                )
            }
        }
    }
}

// --- COMPONENTE NUEVO: DIÁLOGO ---
@Composable
fun AddProviderDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var newName by remember { mutableStateOf("") }
    var newCost by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF333333), // Gris un poco más claro que el fondo
        title = { Text("Nuevo Proveedor", color = BrandYellow, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Nombre Proveedor") },
                    colors = fieldColors(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newCost,
                    onValueChange = { newCost = it },
                    label = { Text("Costo ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = fieldColors(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(newName, newCost) },
                colors = ButtonDefaults.buttonColors(containerColor = BrandYellow)
            ) {
                Text("Agregar", color = Color.Black)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}

// Función auxiliar de colores (igual que antes)
@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextWhite,
    unfocusedTextColor = TextWhite,
    cursorColor = BrandYellow,
    focusedBorderColor = BrandYellow,
    unfocusedBorderColor = Color.Gray,
    focusedLabelColor = BrandYellow,
    unfocusedLabelColor = Color.Gray
)