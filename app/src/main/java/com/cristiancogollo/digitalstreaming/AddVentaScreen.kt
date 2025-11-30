package com.cristiancogollo.digitalstreaming

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSaleScreen(
    navController: NavController,
    viewModel: AddSaleViewModel = viewModel()
) {
    // Control de Diálogos de Selección
    var showClientDialog by remember { mutableStateOf(false) }
    var showProductDialog by remember { mutableStateOf(false) }

    // --- NUEVO: ESTADO DEL CALENDARIO ---
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    currencyFormat.maximumFractionDigits = 0
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Box(
        modifier = Modifier.fillMaxSize().background(DarkBackground).padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            // --- HEADER ---
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextWhite)
                }
                Text("REGISTRAR VENTA", color = BrandYellow, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(20.dp))

            // --- 1. SELECCIONAR CLIENTE ---
            Text("1. Cliente", color = Color.Gray, modifier = Modifier.align(Alignment.Start))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = viewModel.selectedClient?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Seleccionar Cliente...") },
                    trailingIcon = { Icon(Icons.Default.Person, null, tint = BrandYellow) },
                    colors = fieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.matchParentSize().clickable { showClientDialog = true })
            }
            Spacer(modifier = Modifier.height(16.dp))

            // --- NUEVO: 2. FECHA DE VENTA ---
            Text("2. Fecha de Venta (Si fue ayer u otro día)", color = Color.Gray, modifier = Modifier.align(Alignment.Start))
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = dateFormat.format(viewModel.saleDate),
                    onValueChange = {},
                    readOnly = true, // No se escribe, se selecciona
                    trailingIcon = { Icon(Icons.Default.CalendarMonth, null, tint = BrandYellow) },
                    colors = fieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                // Al hacer click, abrimos el calendario
                Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
            }
            Spacer(modifier = Modifier.height(20.dp))

            // --- SWITCH: ¿ES COMBO PERSONALIZADO? ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("¿Crear Combo Manual?", color = TextWhite, fontWeight = FontWeight.Bold)
                Switch(
                    checked = viewModel.isCustomSale,
                    onCheckedChange = { viewModel.isCustomSale = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = BrandYellow
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (!viewModel.isCustomSale) {
                // ==========================================
                // MODO AUTOMÁTICO
                // ==========================================

                Text("3. Producto / Servicio", color = Color.Gray, modifier = Modifier.align(Alignment.Start))
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = viewModel.selectedProduct?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Seleccionar Producto...") },
                        trailingIcon = { Icon(Icons.Default.ShoppingCart, null, tint = BrandYellow) },
                        colors = fieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(modifier = Modifier.matchParentSize().clickable { showProductDialog = true })
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de Proveedor
                if (viewModel.selectedProduct != null) {
                    Text("4. Proveedor", color = Color.Gray, modifier = Modifier.align(Alignment.Start))
                    var expandedProvider by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expandedProvider,
                        onExpandedChange = { expandedProvider = !expandedProvider },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = viewModel.selectedProvider,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = { Text("Seleccione Proveedor") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProvider) },
                            colors = fieldColors(),
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedProvider,
                            onDismissRequest = { expandedProvider = false },
                            modifier = Modifier.background(Color(0xFF333333))
                        ) {
                            viewModel.selectedProduct!!.providers.forEach { (name, cost) ->
                                DropdownMenuItem(
                                    text = {
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text(name, color = TextWhite)
                                            Text(currencyFormat.format(cost), color = Color.Gray, fontSize = 12.sp)
                                        }
                                    },
                                    onClick = {
                                        viewModel.onProviderSelected(name)
                                        expandedProvider = false
                                    }
                                )
                            }
                        }
                    }
                }

            } else {
                // ==========================================
                // MODO MANUAL
                // ==========================================

                OutlinedTextField(
                    value = viewModel.customProductName,
                    onValueChange = { viewModel.customProductName = it },
                    label = { Text("Nombre del Combo/Servicio") },
                    placeholder = { Text("Ej: Netflix + Pornhub + Spotify") },
                    leadingIcon = { Icon(Icons.Default.Edit, null, tint = BrandYellow) },
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                    colors = fieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = viewModel.customCostPrice,
                        onValueChange = { viewModel.customCostPrice = it },
                        label = { Text("Costo Total ($)") },
                        placeholder = { Text("Lo que pagaste") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = fieldColors(),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = viewModel.customServiceDays,
                        onValueChange = { viewModel.customServiceDays = it },
                        label = { Text("Días") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = fieldColors(),
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- RESUMEN Y FINALIZAR ---
            val showSummary = (!viewModel.isCustomSale && viewModel.selectedProvider.isNotEmpty()) ||
                    (viewModel.isCustomSale && viewModel.customProductName.isNotEmpty() && viewModel.customCostPrice.isNotEmpty())

            if (showSummary) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF333333)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Resumen de Venta", color = BrandYellow, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = viewModel.finalSalePrice,
                            onValueChange = { viewModel.finalSalePrice = it },
                            label = { Text("Precio Venta al Cliente") },
                            leadingIcon = { Icon(Icons.Default.AttachMoney, null, tint = TextWhite) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = fieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Cálculos
                        val currentCost = if (viewModel.isCustomSale) (viewModel.customCostPrice.toDoubleOrNull() ?: 0.0) else viewModel.costPrice
                        val profit = (viewModel.finalSalePrice.toDoubleOrNull() ?: 0.0) - currentCost
                        val days = if (viewModel.isCustomSale) (viewModel.customServiceDays.toIntOrNull() ?: 30) else viewModel.selectedProduct!!.serviceDays

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Costo:", color = Color.Gray)
                            Text(currencyFormat.format(currentCost), color = TextWhite)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Ganancia:", color = Color.Gray)
                            Text(currencyFormat.format(profit), color = if(profit >= 0) Color.Green else Color.Red, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))

                        // CÁLCULO DE VENCIMIENTO VISUAL
                        // Usamos la fecha seleccionada en el ViewModel
                        val calendar = Calendar.getInstance()
                        calendar.time = viewModel.saleDate
                        calendar.add(Calendar.DAY_OF_YEAR, days)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarToday, null, tint = BrandYellow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Vence: ${dateFormat.format(calendar.time)}", color = TextWhite, fontWeight = FontWeight.Bold)
                            Text(" ($days días)", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { viewModel.saveSale { navController.popBackStack() } },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandYellow),
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !viewModel.isLoading
                ) {
                    Text("FINALIZAR VENTA", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // --- NUEVO: DIÁLOGO DE CALENDARIO ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Ajuste de zona horaria para que no se corra el día
                        val offset = TimeZone.getDefault().getOffset(millis)
                        viewModel.saleDate = Date(millis + offset)
                    }
                    showDatePicker = false
                }) {
                    Text("Aceptar", color = BrandYellow)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = Color.White)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFF282828), // Fondo oscuro para el calendario
                titleContentColor = BrandYellow,
                headlineContentColor = TextWhite,
                weekdayContentColor = BrandYellow,
                dayContentColor = TextWhite,
                selectedDayContainerColor = BrandYellow,
                selectedDayContentColor = Color.Black,
                yearContentColor = TextWhite
            )
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Diálogos de Selección (Cliente y Producto)
    if (showClientDialog) {
        SearchSelectionDialog(
            title = "Seleccionar Cliente",
            items = viewModel.clientsList,
            itemContent = { client ->
                Column {
                    Text(client.name, color = TextWhite, fontWeight = FontWeight.Bold)
                    Text(client.phone, color = Color.Gray, fontSize = 12.sp)
                }
            },
            onItemSelected = {
                viewModel.selectedClient = it
                showClientDialog = false
            },
            onDismiss = { showClientDialog = false },
            filterPredicate = { item, query -> item.name.contains(query, true) || item.phone.contains(query, true) }
        )
    }

    if (showProductDialog) {
        SearchSelectionDialog(
            title = "Seleccionar Producto",
            items = viewModel.productsList,
            itemContent = { product ->
                Column {
                    Text(product.name, color = TextWhite, fontWeight = FontWeight.Bold)
                    Text("$ ${product.salePrice.toInt()}", color = BrandYellow, fontSize = 12.sp)
                }
            },
            onItemSelected = {
                viewModel.onProductSelected(it)
                showProductDialog = false
            },
            onDismiss = { showProductDialog = false },
            filterPredicate = { item, query -> item.name.contains(query, true) }
        )
    }
}

// --- COMPONENTE REUTILIZABLE: DIÁLOGO CON BUSCADOR ---
@Composable
fun <T> SearchSelectionDialog(
    title: String,
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    onItemSelected: (T) -> Unit,
    onDismiss: () -> Unit,
    filterPredicate: (T, String) -> Boolean
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = items.filter { filterPredicate(it, searchQuery) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF282828),
        title = { Text(title, color = BrandYellow) },
        text = {
            Column(modifier = Modifier.height(400.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar...", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = Color.Gray) },
                    colors = fieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn {
                    items(filteredItems) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemSelected(item) }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            itemContent(item)
                        }
                        Divider(color = Color.Gray.copy(alpha = 0.3f))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar", color = BrandYellow) }
        }
    )
}