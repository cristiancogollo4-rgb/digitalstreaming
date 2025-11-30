package com.cristiancogollo.digitalstreaming

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClientScreen(
    navController: NavController,
    viewModel: AddClientViewModel = viewModel()
) {
    val context = LocalContext.current

    // Estados para controlar los diálogos
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showDuplicateAlert by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextWhite)
                }
                Text("NUEVO CLIENTE", color = BrandYellow, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(30.dp))

            // --- CAMPOS DE TEXTO (Igual que antes) ---
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text("Nombre Completo") },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                colors = fieldColors(), modifier = Modifier.fillMaxWidth(), singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.phone,
                onValueChange = { if (it.all { char -> char.isDigit() }) viewModel.phone = it },
                label = { Text("Teléfono") },
                leadingIcon = { Text("+57 ", color = TextWhite, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start=12.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                colors = fieldColors(), modifier = Modifier.fillMaxWidth(), singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Tipo de Cliente
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { viewModel.clientType = "Consumidor" }) {
                    RadioButton(selected = viewModel.clientType == "Consumidor", onClick = { viewModel.clientType = "Consumidor" }, colors = RadioButtonDefaults.colors(selectedColor = BrandYellow, unselectedColor = Color.Gray))
                    Text("Consumidor", color = TextWhite)
                }
                Spacer(modifier = Modifier.width(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { viewModel.clientType = "Revendedor" }) {
                    RadioButton(selected = viewModel.clientType == "Revendedor", onClick = { viewModel.clientType = "Revendedor" }, colors = RadioButtonDefaults.colors(selectedColor = BrandYellow, unselectedColor = Color.Gray))
                    Text("Revendedor", color = TextWhite)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.notes, onValueChange = { viewModel.notes = it },
                label = { Text("Notas (Opcional)") },
                colors = fieldColors(), modifier = Modifier.fillMaxWidth(), minLines = 3
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- BOTÓN GUARDAR MODIFICADO ---
            Button(
                onClick = {
                    // 1. PRIMERO verificamos si existe
                    viewModel.checkIfClientExists { exists ->
                        if (exists) {
                            showDuplicateAlert = true // Si existe, alerta de error
                        } else {
                            showConfirmDialog = true // Si no, pregunta confirmación
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandYellow, disabledContainerColor = Color.Gray),
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                // Deshabilitado si faltan datos o si está cargando
                enabled = viewModel.name.isNotEmpty() && viewModel.phone.isNotEmpty() && !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                } else {
                    Text("GUARDAR CLIENTE", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- DIÁLOGO 1: ALERTA DE DUPLICADO ---
        if (showDuplicateAlert) {
            AlertDialog(
                onDismissRequest = { showDuplicateAlert = false },
                icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = BrandYellow) },
                title = { Text("¡Cliente Duplicado!", fontWeight = FontWeight.Bold) },
                text = { Text("Ya existe un cliente con este nombre o número de teléfono. Verifica los datos.") },
                confirmButton = {
                    Button(onClick = { showDuplicateAlert = false }, colors = ButtonDefaults.buttonColors(containerColor = BrandYellow)) {
                        Text("Entendido", color = Color.Black)
                    }
                },
                containerColor = Color(0xFF333333),
                titleContentColor = BrandYellow,
                textContentColor = TextWhite
            )
        }

        // --- DIÁLOGO 2: CONFIRMACIÓN DE GUARDADO ---
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = { Text("¿Guardar Cliente?", fontWeight = FontWeight.Bold) },
                text = { Text("Se registrará a ${viewModel.name} como ${viewModel.clientType}.") },
                confirmButton = {
                    Button(
                        onClick = {
                            showConfirmDialog = false
                            // 2. Si confirma, GUARDAMOS
                            viewModel.saveClient {
                                Toast.makeText(context, "Cliente guardado", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandYellow)
                    ) {
                        Text("Sí, Guardar", color = Color.Black)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmDialog = false }) {
                        Text("Cancelar", color = Color.Gray)
                    }
                },
                containerColor = Color(0xFF333333),
                titleContentColor = BrandYellow,
                textContentColor = TextWhite
            )
        }
    }
}