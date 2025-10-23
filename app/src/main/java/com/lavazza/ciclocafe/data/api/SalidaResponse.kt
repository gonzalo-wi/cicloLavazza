package com.lavazza.ciclocafe.data.api

/**
 * Modelo de respuesta para el endpoint POST /products/out
 */
data class SalidaResponse(
    val success: Boolean,
    val message: String,
    val data: Any? = null
)
