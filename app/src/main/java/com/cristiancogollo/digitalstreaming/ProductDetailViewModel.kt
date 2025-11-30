package com.cristiancogollo.digitalstreaming


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ProductDetailViewModel : ViewModel() {

    // Estado del producto
    var name by mutableStateOf("")
    var salePrice by mutableStateOf("")
    var imageUrl by mutableStateOf("")
    var serviceDays by mutableStateOf("")

    // Para los proveedores (Usamos un mapa mutable para poder editar costos)
    var providers = mutableStateMapOf<String, String>()

    var isLoading by mutableStateOf(true)
    var saveStatus by mutableStateOf<String?>(null)

    private val db = FirebaseFirestore.getInstance()

    // Cargar producto por ID
    fun loadProduct(productId: String) {
        isLoading = true
        db.collection("products").document(productId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val product = document.toObject(ProductModel::class.java)
                    product?.let {
                        name = it.name
                        salePrice = it.salePrice.toInt().toString() // Sin decimales para editar facil
                        imageUrl = it.imageUrl
                        serviceDays = it.serviceDays.toString()

                        // Convertimos el mapa de Double a String para los TextField
                        it.providers.forEach { (key, value) ->
                            providers[key] = value.toInt().toString()
                        }
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }
    }

    // Guardar cambios
    fun updateProduct(productId: String, onSuccess: () -> Unit) {
        val updates = hashMapOf<String, Any>(
            "name" to name,
            "salePrice" to (salePrice.toDoubleOrNull() ?: 0.0),
            "serviceDays" to (serviceDays.toIntOrNull() ?: 30),
            "imageUrl" to imageUrl,
            // Convertimos el mapa de strings de vuelta a doubles
            "providers" to providers.mapValues { it.value.toDoubleOrNull() ?: 0.0 }
        )

        db.collection("products").document(productId)
            .update(updates)
            .addOnSuccessListener {
                saveStatus = "¡Producto actualizado!"
                onSuccess()
            }
            .addOnFailureListener { e ->
                saveStatus = "Error: ${e.message}"
            }
    }

    // Eliminar producto (Opcional pero útil)
    fun deleteProduct(productId: String, onSuccess: () -> Unit) {
        db.collection("products").document(productId)
            .delete()
            .addOnSuccessListener { onSuccess() }
    }
    fun addProvider(name: String, cost: String) {
        if (name.isNotBlank() && cost.isNotBlank()) {
            providers[name] = cost
        }
    }

    // Función para eliminar un proveedor específico (Opcional pero útil)
    fun removeProvider(name: String) {
        providers.remove(name)
    }
}