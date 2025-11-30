package com.cristiancogollo.digitalstreaming

data class ProductModel(
    val id: String = "",
    val name: String = "",
    val salePrice: Double = 0.0,
    val providers: Map<String, Double> = emptyMap(),
    val serviceDays: Int = 30,
    val imageUrl: String = "" // Nuevo campo para la foto
)