package com.cristiancogollo.digitalstreaming
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.google.ai.client.generativeai.Chat


// --- 1. Definición de Colores ---
val DarkBackground = Color(0xFF282828) // Gris oscuro casi negro
val BrandYellow = Color(0xFFFFE066)    // Amarillo tipo "Digital Streaming"
val TextWhite = Color(0xFFEEEEEE)

@Composable
fun DigitalStreamingApp() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        HeaderSection()
        Spacer(modifier = Modifier.height(40.dp))
        MenuGridSection()

        Spacer(modifier = Modifier.weight(1f)) // Empuja hacia abajo

        // --- AQUI IMPLEMENTAMOS EL COMPONENTE REUTILIZABLE ---
        SharedBottomNavigation(
            onNotificationClick = { /* Acción notificaciones */ },
            onHomeClick = { /* Acción Home */ },
            onStatsClick = { /* Acción Estadísticas */ }
        )
    }
}

// --- Componentes Modulares ---

@Composable
fun HeaderSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        // Icono de Claqueta (Simulado con Movie)
        Icon(
            imageVector = Icons.Default.Movie,
            contentDescription = "Logo",
            tint = TextWhite,
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = "Digital",
                color = TextWhite,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "STREAMING",
                color = BrandYellow,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun MenuGridSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.padding(horizontal = 30.dp)
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        // Fila 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            MenuCard(
                title = "Clientes",
                icon = Icons.Default.Person,
                modifier = Modifier.weight(1f)
            )
            MenuCard(
                title = "Productos",
                icon = Icons.Outlined.PlayCircle,
                modifier = Modifier.weight(1f)
            )
        }
        //Spacer(modifier = Modifier.height(25.dp))
        // Fila 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            MenuCard(
                title = "Ventas",
                icon = Icons.Default.ConfirmationNumber, // Icono tipo Ticket
                modifier = Modifier.weight(1f)
            )
            MenuCard(
                title = "Programar\nMensaje",
                icon = Icons.Default.Chat,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MenuCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    // Diseño de cada botón grande
    Card(
        modifier = modifier.aspectRatio(0.85f), // Mantiene proporción vertical rectangular
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.5.dp, BrandYellow)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = BrandYellow,
                modifier = Modifier.size(55.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                color = TextWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}



// --- 5. Implementación de Preview ---
@Preview(showBackground = true)
@Composable
fun PreviewDigitalStreaming() {
    MaterialTheme {
        DigitalStreamingApp()
    }
}