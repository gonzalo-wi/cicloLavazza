package com.lavazza.ciclocafe.data.api

/**
 * Modelo de respuesta para el endpoint POST /products/in
 */
data class EntradaResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null
)

