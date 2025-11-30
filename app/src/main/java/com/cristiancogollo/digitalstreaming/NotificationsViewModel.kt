package com.cristiancogollo.digitalstreaming

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import java.util.Date
import java.util.concurrent.TimeUnit

class NotificationsViewModel : ViewModel() {

    var expiringSales by mutableStateOf<List<SaleModel>>(emptyList())
    var isLoading by mutableStateOf(true)
    private val db = FirebaseFirestore.getInstance()

    init {
        loadExpiringSales()
    }

    private fun loadExpiringSales() {
        db.collection("sales")
            .whereEqualTo("status", "Activa")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val allSales = snapshot.toObjects<SaleModel>()
                    val today = Date()

                    expiringSales = allSales.filter { sale ->
                        val diff = sale.expiryDate.time - today.time
                        val days = TimeUnit.MILLISECONDS.toDays(diff)
                        days <= 2
                    }.sortedBy { it.expiryDate }

                    isLoading = false
                }
            }
    }

    // --- NUEVA FUNCIÓN: Buscar teléfono al instante ---
    fun getClientPhone(clientId: String, onResult: (String?) -> Unit) {
        db.collection("clients").document(clientId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Devolvemos el teléfono encontrado
                    onResult(document.getString("phone"))
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}