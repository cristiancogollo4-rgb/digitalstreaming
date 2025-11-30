package com.cristiancogollo.digitalstreaming

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore

class AddClientViewModel : ViewModel() {
    var name by mutableStateOf("")
    var phone by mutableStateOf("")
    var email by mutableStateOf("") // Opcional
    var clientType by mutableStateOf("Consumidor")
    var notes by mutableStateOf("")

    var statusMsg by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false) // Para bloquear el botón mientras busca

    private val db = FirebaseFirestore.getInstance()

    // --- NUEVA FUNCIÓN: Verificar duplicados ---
    fun checkIfClientExists(onResult: (Boolean) -> Unit) {
        isLoading = true

        // Busca si hay alguien con EL MISMO nombre O EL MISMO teléfono
        val query = db.collection("clients")
            .where(Filter.or(
                Filter.equalTo("name", name.trim()),
                Filter.equalTo("phone", phone.trim())
            ))

        query.get()
            .addOnSuccessListener { documents ->
                isLoading = false
                // Si la lista NO está vacía, es porque YA EXISTE alguien
                val exists = !documents.isEmpty
                onResult(exists)
            }
            .addOnFailureListener {
                isLoading = false
                statusMsg = "Error al verificar: ${it.message}"
                onResult(false) // Asumimos falso para no bloquear, pero mostramos error
            }
    }

    fun saveClient(onSuccess: () -> Unit) {
        isLoading = true
        val docRef = db.collection("clients").document()

        val client = ClientModel(
            id = docRef.id,
            name = name.trim(),
            phone = phone.trim(),
            clientType = clientType,
            notes = notes.trim()
        )

        docRef.set(client)
            .addOnSuccessListener {
                isLoading = false
                statusMsg = "Cliente guardado con éxito"
                onSuccess()
            }
            .addOnFailureListener { e ->
                isLoading = false
                statusMsg = "Error al guardar: ${e.message}"
            }
    }
}