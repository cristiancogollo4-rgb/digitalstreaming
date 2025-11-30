package com.cristiancogollo.digitalstreaming

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import java.util.Calendar
import java.util.Date

class AddSaleViewModel : ViewModel() {

    // Listas
    var clientsList by mutableStateOf<List<ClientModel>>(emptyList())
    var productsList by mutableStateOf<List<ProductModel>>(emptyList())

    // Modo Venta Regular
    var selectedClient by mutableStateOf<ClientModel?>(null)
    var selectedProduct by mutableStateOf<ProductModel?>(null)
    var selectedProvider by mutableStateOf("")

    // Modo Manual
    var isCustomSale by mutableStateOf(false)
    var customProductName by mutableStateOf("")
    var customCostPrice by mutableStateOf("")
    var customServiceDays by mutableStateOf("30")

    // Datos Comunes
    var finalSalePrice by mutableStateOf("")
    var costPrice by mutableStateOf(0.0)

    // --- NUEVO: FECHA DE VENTA (Por defecto HOY) ---
    var saleDate by mutableStateOf(Date())

    var isLoading by mutableStateOf(false)
    var statusMsg by mutableStateOf<String?>(null)

    private val db = FirebaseFirestore.getInstance()

    init {
        loadData()
    }

    private fun loadData() {
        db.collection("clients").get().addOnSuccessListener { clientsList = it.toObjects() }
        db.collection("products").get().addOnSuccessListener { productsList = it.toObjects() }
    }

    fun onProductSelected(product: ProductModel) {
        selectedProduct = product
        selectedProvider = ""
        finalSalePrice = product.salePrice.toInt().toString()
        costPrice = 0.0
    }

    fun onProviderSelected(providerName: String) {
        selectedProvider = providerName
        costPrice = selectedProduct?.providers?.get(providerName) ?: 0.0
    }

    fun saveSale(onSuccess: () -> Unit) {
        if (selectedClient == null) return

        if (!isCustomSale && (selectedProduct == null || selectedProvider.isEmpty())) return
        if (isCustomSale && (customProductName.isEmpty() || customCostPrice.isEmpty())) return

        isLoading = true
        val docRef = db.collection("sales").document()

        val saleP = finalSalePrice.toDoubleOrNull() ?: 0.0

        val finalProductName: String
        val finalProvider: String
        val finalCost: Double
        val days: Int
        val prodId: String

        if (isCustomSale) {
            finalProductName = customProductName
            finalProvider = "Personalizado"
            finalCost = customCostPrice.toDoubleOrNull() ?: 0.0
            days = customServiceDays.toIntOrNull() ?: 30
            prodId = "custom_combo"
        } else {
            finalProductName = selectedProduct!!.name
            finalProvider = selectedProvider
            finalCost = costPrice
            days = selectedProduct!!.serviceDays
            prodId = selectedProduct!!.id
        }

        val profit = saleP - finalCost

        // --- CÁLCULO DE VENCIMIENTO BASADO EN LA FECHA SELECCIONADA ---
        val calendar = Calendar.getInstance()
        calendar.time = saleDate // Usamos la fecha elegida (ayer, hoy, etc.)
        calendar.add(Calendar.DAY_OF_YEAR, days)
        val expiryDate = calendar.time

        val sale = SaleModel(
            id = docRef.id,
            clientId = selectedClient!!.id,
            clientName = selectedClient!!.name,
            productId = prodId,
            productName = finalProductName,
            providerName = finalProvider,
            salePrice = saleP,
            costPrice = finalCost,
            profit = profit,
            saleDate = saleDate, // Guardamos la fecha elegida
            expiryDate = expiryDate,
            status = "Activa"
        )

        docRef.set(sale)
            .addOnSuccessListener {
                isLoading = false
                statusMsg = "¡Venta registrada!"
                onSuccess()
            }
            .addOnFailureListener {
                isLoading = false
                statusMsg = "Error: ${it.message}"
            }
    }
}