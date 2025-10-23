package com.lavazza.ciclocafe.data.model

/**
 * Modelo de datos para el request de salida al endpoint
 * POST http://192.168.0.12/api/cafe/products/out
 */
data class SalidaRequest(
    val products: List<ProductExit>,
    val idReparto: Int
)

data class ProductExit(
    val idProducto: String,
    val vta_total: Int,
    val vta_lleno: Int,
    val recambio: Int
)

