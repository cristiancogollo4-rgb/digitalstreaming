package com.cristiancogollo.digitalstreaming

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects

class ClientsViewModel : ViewModel() {

    // Lista completa
    var clientList by mutableStateOf<List<ClientModel>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    // Buscador
    var searchQuery by mutableStateOf("")

    // Lista Filtrada (Busca por Nombre o Teléfono)
    val filteredClients: List<ClientModel>
        get() {
            if (searchQuery.isEmpty()) return clientList
            return clientList.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.phone.contains(searchQuery, ignoreCase = true)
            }
        }

    init {
        fetchClients()
    }

    private fun fetchClients() {
        val db = FirebaseFirestore.getInstance()
        db.collection("clients")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    isLoading = false
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    clientList = snapshot.toObjects<ClientModel>()
                    isLoading = false
                }
            }
    }
}