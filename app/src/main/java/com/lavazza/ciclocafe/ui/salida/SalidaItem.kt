package com.lavazza.ciclocafe.ui.salida

import com.lavazza.ciclocafe.data.model.Product

data class SalidaItem(
    var producto: String = "",
    var selectedProduct: Product? = null,
    var vueltaLleno: Int = 0,
    var vueltaTotal: Int = 0,
    var recambio: Int = 0
)

