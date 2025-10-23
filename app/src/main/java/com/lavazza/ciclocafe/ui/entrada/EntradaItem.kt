package com.lavazza.ciclocafe.ui.entrada

import com.lavazza.ciclocafe.data.model.Product

data class EntradaItem(
    var producto: String = "",
    var selectedProduct: Product? = null,
    var vueltaLleno: Int = 0,
    var vueltaTotal: Int = 0,
    var recambio: Int = 0
)
