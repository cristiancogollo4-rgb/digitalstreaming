package com.cristiancogollo.digitalstreaming

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationsViewModel = viewModel()
) {
    val sales = viewModel.expiringSales
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
                Icon(Icons.Filled.Notifications, null, tint = BrandYellow, modifier = Modifier.size(40.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("POR VENCER", color = TextWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
            }

            Text("Clientes a cobrar hoy/mañana", color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BrandYellow)
                }
            } else if (sales.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.Notifications, null, tint = Color.DarkGray, modifier = Modifier.size(60.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("¡Todo al día! 🎉", color = TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("No hay vencimientos próximos.", color = Color.Gray)
                    }
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
                        // Pasamos el viewModel a la tarjeta para que pueda hacer la búsqueda
                        NotificationCard(sale, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    sale: SaleModel,
    viewModel: NotificationsViewModel // Necesitamos el VM para buscar el cliente
) {
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())

    val diff = sale.expiryDate.time - Date().time
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    val urgencyText = when {
        days < 0 -> "VENCIDA"
        days == 0L -> "HOY"
        days == 1L -> "MAÑANA"
        else -> "En $days días"
    }
    val urgencyColor = if (days < 0) Color.Red else BrandYellow

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D2D)),
        border = BorderStroke(1.dp, urgencyColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(sale.clientName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Surface(color = urgencyColor.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                    Text(urgencyText, color = urgencyColor, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(sale.productName, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.Gray.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // BOTÓN INTELIGENTE
            Button(
                onClick = {
                    // 1. BUSCAMOS EL TELÉFONO EN FIREBASE AL DAR CLIC
                    viewModel.getClientPhone(sale.clientId) { phone ->
                        if (!phone.isNullOrEmpty()) {
                            // 2. SI LO ENCONTRAMOS, ABRIMOS WHATSAPP
                            try {
                                val fullPhone = "57$phone"
                                val msg = "Hola ${sale.clientName}! 👋 Te escribo de Digital Streaming. Paso a recordarte que tu cuenta de *${sale.productName}* vence el ${dateFormat.format(sale.expiryDate)}. \n\n¿Deseas renovar el servicio? 🍿"
                                val url = "https://api.whatsapp.com/send?phone=$fullPhone&text=${Uri.encode(msg)}"

                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(url)
                                    setPackage("com.whatsapp.w4b") // Intenta Business
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // Fallback a Whatsapp normal o Web
                                try {
                                    val fullPhone = "57$phone"
                                    val msg = "Hola ${sale.clientName}..." // (Mismo mensaje)
                                    val url = "https://api.whatsapp.com/send?phone=$fullPhone&text=${Uri.encode(msg)}"
                                    val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(fallbackIntent)
                                } catch (e2: Exception) {
                                    Toast.makeText(context, "No se pudo abrir WhatsApp", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            // 3. SI NO LO ENCONTRAMOS (Cliente borrado o error)
                            Toast.makeText(context, "Error: No se encontró el número del cliente", Toast.LENGTH_LONG).show()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = BrandYellow),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Filled.Send, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("COBRAR / RECORDAR", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}