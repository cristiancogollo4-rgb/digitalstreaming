package com.cristiancogollo.digitalstreaming

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects

class SalesViewModel : ViewModel() {

    // Lista de ventas real
    var salesList by mutableStateOf<List<SaleModel>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    // Buscador (Opcional, por ahora buscamos por nombre de cliente)
    var searchQuery by mutableStateOf("")

    // Lista filtrada
    val filteredSales: List<SaleModel>
        get() {
            if (searchQuery.isEmpty()) return salesList
            return salesList.filter {
                it.clientName.contains(searchQuery, ignoreCase = true) ||
                        it.productName.contains(searchQuery, ignoreCase = true)
            }
        }

    init {
        fetchSales()
    }

    private fun fetchSales() {
        val db = FirebaseFirestore.getInstance()

        // Traemos la colección "sales" ordenada por fecha de venta descendente
        db.collection("sales")
            .orderBy("saleDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    isLoading = false
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    salesList = snapshot.toObjects<SaleModel>()
                    isLoading = false
                }
            }
    }
}