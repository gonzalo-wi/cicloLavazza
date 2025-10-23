package com.lavazza.ciclocafe.data.model

/**
 * DTO para POST /products/out
 * { products: [{ idProducto, total }], idReparto }
 */
data class OutRequest(
    val products: List<OutProduct>,
    val idReparto: Int
)

data class OutProduct(
    val idProducto: String,
    val total: Int
)

