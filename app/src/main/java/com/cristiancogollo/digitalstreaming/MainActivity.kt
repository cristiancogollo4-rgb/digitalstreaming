package com.cristiancogollo.digitalstreaming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.cristiancogollo.digitalstreaming.ui.theme.DigitalstreamingTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            // 1. Creamos el controlador
            val navController = rememberNavController()

            // 2. Estructura base de la app
            Scaffold(
                bottomBar = {
                    // Aquí llamamos a tu barra, pasándole el controlador
                    SharedBottomNavigation(navController = navController)
                }
            ) { paddingValues ->
                // 3. El contenido cambiante (paddingValues evita que la barra tape el contenido)
                Box(modifier = Modifier.padding(paddingValues)) {
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}