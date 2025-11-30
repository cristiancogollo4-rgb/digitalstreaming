package com.cristiancogollo.digitalstreaming


import java.util.Date

data class SaleModel(
    val id: String = "",
    // Relaciones
    val clientId: String = "",
    val clientName: String = "", // Snapshot (nombre en el momento de la venta)
    val productId: String = "",
    val productName: String = "", // Snapshot

    // Datos Financieros
    val providerName: String = "", // Quién nos lo vendió (ej: Dismanet)
    val salePrice: Double = 0.0,   // En cuánto lo vendí
    val costPrice: Double = 0.0,   // Cuánto me costó
    val profit: Double = 0.0,      // Ganancia (Calculada)

    // Fechas
    val saleDate: Date = Date(),   // Fecha hoy
    val expiryDate: Date = Date(), // Fecha vencimiento calculada

    val status: String = "Activa" // "Activa", "Por Vencer", "Vencida"
)