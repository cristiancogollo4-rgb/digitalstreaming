package com.cristiancogollo.digitalstreaming

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

class ProductsViewModel : ViewModel() {

    // Lista original completa (Fuente de la verdad)
    var productList by mutableStateOf<List<ProductModel>>(emptyList())
        private set

    // Estado de carga
    var isLoading by mutableStateOf(true)
        private set

    // --- NUEVO: Texto del buscador ---
    var searchQuery by mutableStateOf("")

    // --- NUEVO: Lista Filtrada (Calculada) ---
    // Si el buscador está vacío, devuelve todo. Si no, filtra por nombre.
    val filteredProducts: List<ProductModel>
        get() {
            if (searchQuery.isEmpty()) return productList
            return productList.filter {
                it.name.contains(searchQuery, ignoreCase = true) // Ignora mayúsculas/minúsculas
            }
        }

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        val db = FirebaseFirestore.getInstance()

        db.collection("products")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    isLoading = false
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    productList = snapshot.toObjects<ProductModel>()
                    isLoading = false
                }
            }
    }
}