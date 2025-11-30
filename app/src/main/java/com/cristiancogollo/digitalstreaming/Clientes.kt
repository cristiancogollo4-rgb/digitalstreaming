package com.cristiancogollo.digitalstreaming

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ClientsScreen(
    navController: NavController,
    viewModel: ClientsViewModel = viewModel()
) {
    val clients = viewModel.filteredClients
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
            // Header
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = TextWhite, modifier = Modifier.size(50.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("CLIENTES", color = TextWhite, fontSize = 34.sp, fontWeight = FontWeight.Black)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Buscador
            SearchBarClients(
                query = viewModel.searchQuery,
                onQueryChange = { viewModel.searchQuery = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Lista
            if (isLoading) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BrandYellow)
                }
            } else if (clients.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text("No hay clientes registrados", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(clients) { client ->
                        ClientCard(client)
                    }
                }
            }
        }

        // FAB Agregar
        FloatingActionButton(
            onClick = { navController.navigate(AppScreens.AddClient.route) },
            containerColor = BrandYellow,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 90.dp, end = 20.dp)
                .size(65.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar", tint = Color.Black)
        }
    }
}

@Composable
fun ClientCard(client: ClientModel) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, BrandYellow.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // DATOS DEL CLIENTE
            Column(modifier = Modifier.weight(1f)) {
                Text(client.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)

                Text(
                    text = client.clientType,
                    color = if(client.clientType == "Revendedor") BrandYellow else Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

            }

            // --- BOTÓN DE WHATSAPP BUSINESS ---
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Transparent,
                modifier = Modifier
                    .clickable {
                        try {
                            // 1. Preparamos el número
                            val phoneNumber = "57${client.phone}"
                            val url = "https://api.whatsapp.com/send?phone=$phoneNumber"

                            // 2. Creamos el Intent
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(url)
                                // ESTA LÍNEA ES LA CLAVE: Forzamos el paquete de Business
                                setPackage("com.whatsapp.w4b")
                            }

                            // 3. Intentamos abrir Business
                            context.startActivity(intent)

                        } catch (e: Exception) {
                            // 4. Si falla (ej: no tienes Business instalado), intentamos abrir el WhatsApp normal o navegador
                            try {
                                val phoneNumber = "57${client.phone}"
                                val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
                                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(fallbackIntent)
                            } catch (e2: Exception) {
                                Toast.makeText(context, "No tienes WhatsApp instalado", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = client.phone,
                        color = BrandYellow,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Filled.Phone,
                        contentDescription = "WhatsApp Business",
                        tint = BrandYellow,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBarClients(query: String, onQueryChange: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Buscar por nombre o cel...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = BrandYellow) },
        trailingIcon = {
            if(query.isNotEmpty()) IconButton(onClick = { onQueryChange("") }) { Icon(Icons.Filled.Close, null, tint = Color.Gray) }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = TextWhite,
            unfocusedTextColor = TextWhite,
            cursorColor = BrandYellow,
            focusedIndicatorColor = BrandYellow,
            unfocusedIndicatorColor = BrandYellow.copy(alpha = 0.5f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}