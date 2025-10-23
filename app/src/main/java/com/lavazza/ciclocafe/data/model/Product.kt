package com.lavazza.ciclocafe.data.model

data class Product(
    val idProducto: String,
    val name: String,
    val pack: String
)

data class ProductResponse(
    val success: Boolean,
    val data: List<Product>,
    val message: String
)

