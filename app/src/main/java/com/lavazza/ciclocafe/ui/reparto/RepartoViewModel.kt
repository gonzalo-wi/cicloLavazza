package com.lavazza.ciclocafe.ui.reparto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RepartoViewModel : ViewModel() {

    private val _numeroReparto = MutableLiveData<String>()
    val numeroReparto: LiveData<String> = _numeroReparto

    fun setNumeroReparto(numero: String) {
        _numeroReparto.value = numero
    }
}

