package com.lavazza.ciclocafe.data.model

/**
 * Modelo de datos para el request de entrada al endpoint
 * POST http://192.168.0.12/api/cafe/products/in
 */
data class EntradaRequest(
    val products: List<ProductEntry>,
    val idReparto: Int
)

data class ProductEntry(
    val idProducto: String,
    val vta_total: Int,
    val vta_lleno: Int,
    val recambio: Int
)

