package com.lavazza.ciclocafe.ui.salida

import com.lavazza.ciclocafe.data.model.Product

data class SalidaItem(
    var producto: String = "",
    var selectedProduct: Product? = null,
    var total: Int = 0
)
