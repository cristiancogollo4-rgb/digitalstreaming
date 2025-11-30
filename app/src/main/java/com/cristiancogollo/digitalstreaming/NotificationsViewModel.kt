package com.cristiancogollo.digitalstreaming


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class NotificationsViewModel : ViewModel() {

    var expiringSales by mutableStateOf<List<SaleModel>>(emptyList())
    var isLoading by mutableStateOf(true)

    init {
        loadExpiringSales()
    }

    private fun loadExpiringSales() {
        val db = FirebaseFirestore.getInstance()

        // Traemos las ventas "Activas"
        // (En una app masiva, esto se filtra en el servidor, pero para tu uso local está bien filtrar aquí)
        db.collection("sales")
            .whereEqualTo("status", "Activa")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    val allSales = snapshot.toObjects<SaleModel>()

                    // FILTRO INTELIGENTE:
                    // Solo las que vencen en los próximos 3 días (o ya vencieron hace poco y no se han cerrado)
                    val today = Date()

                    expiringSales = allSales.filter { sale ->
                        val diff = sale.expiryDate.time - today.time
                        val days = TimeUnit.MILLISECONDS.toDays(diff)

                        // Mostramos: Vencidas (negativo) y las que vencen en 3 días o menos
                        days <= 2
                    }.sortedBy { it.expiryDate } // Ordenar por la más urgente

                    isLoading = false
                }
            }
    }
}