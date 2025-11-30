package com.cristiancogollo.digitalstreaming

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    viewModel: AddProductViewModel = viewModel() // Inyectamos el ViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- HEADER con Botón Atrás ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = BrandYellow)
                }
                Text(
                    text = "NUEVO SERVICIO",
                    color = TextWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- CAMPOS DE TEXTO ---

            // 1. Nombre
            CustomTextField(value = viewModel.name, onValueChange = { viewModel.name = it }, label = "Nombre del Servicio (Ej: Netflix)")

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Precio Venta
            CustomTextField(
                value = viewModel.salePrice,
                onValueChange = { viewModel.salePrice = it },
                label = "Precio Venta ($)",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Precio Costo
            CustomTextField(
                value = viewModel.costPrice,
                onValueChange = { viewModel.costPrice = it },
                label = "Precio Costo ($)",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Proveedor
            CustomTextField(value = viewModel.provider, onValueChange = { viewModel.provider = it }, label = "Proveedor")

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Días
            CustomTextField(
                value = viewModel.serviceDays,
                onValueChange = { viewModel.serviceDays = it },
                label = "Días de Servicio",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(40.dp))

            // --- BOTÓN GUARDAR ---
            Button(
                onClick = {
                    viewModel.addProductToFirebase {
                        navController.popBackStack() // Volver a la lista al terminar
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandYellow),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("GUARDAR PRODUCTO", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            // Mensaje de estado (opcional)
            if (viewModel.saveStatus != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = viewModel.saveStatus ?: "", color = BrandYellow)
            }
            Button(
                onClick = {
                    DataSeeder.uploadInitialData { mensaje ->
                        // Mostramos el resultado en el estado del ViewModel para verlo en pantalla
                        viewModel.saveStatus = mensaje
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Rojo para que sepas que es temporal
            ) {
                Text("CARGAR BASE DE DATOS INICIAL")
            }
        }
    }
}

// Componente para no repetir código en los inputs
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BrandYellow, RoundedCornerShape(12.dp)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite,
            cursorColor = BrandYellow,
            focusedBorderColor = Color.Transparent, // Usamos el borde del modifier
            unfocusedBorderColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp)
    )
}
