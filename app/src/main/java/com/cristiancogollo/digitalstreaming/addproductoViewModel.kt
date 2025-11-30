package com.cristiancogollo.digitalstreaming

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class AddProductViewModel : ViewModel() {

    // Estados para los campos de texto
    var name by mutableStateOf("")
    var salePrice by mutableStateOf("")
    var costPrice by mutableStateOf("")
    var provider by mutableStateOf("")
    var serviceDays by mutableStateOf("30") // 30 días por defecto

    // Estado para saber si se guardó correctamente
    var saveStatus by mutableStateOf<String?>(null)

    fun addProductToFirebase(onSuccess: () -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Crear el objeto mapa para enviar a Firebase
        val productData = hashMapOf(
            "name" to name,
            "salePrice" to (salePrice.toDoubleOrNull() ?: 0.0),
            "costPrice" to (costPrice.toDoubleOrNull() ?: 0.0),
            "provider" to provider,
            "serviceDays" to (serviceDays.toIntOrNull() ?: 30)
        )

        // Guardar en la colección "products"
        db.collection("products")
            .add(productData)
            .addOnSuccessListener {
                saveStatus = "Producto guardado con éxito"
                // Limpiar campos
                name = ""
                salePrice = ""
                costPrice = ""
                provider = ""
                onSuccess() // Navegar atrás
            }
            .addOnFailureListener { e ->
                saveStatus = "Error: ${e.message}"
            }
    }
}