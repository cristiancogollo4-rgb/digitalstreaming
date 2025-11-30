package com.cristiancogollo.digitalstreaming
data class ClientModel(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val clientType: String = "Consumidor", // Valor por defecto
    val notes: String = ""
)