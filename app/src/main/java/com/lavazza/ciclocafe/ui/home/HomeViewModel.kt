package com.lavazza.ciclocafe.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _entradasCount = MutableLiveData<Int>().apply {
        value = 0
    }
    val entradasCount: LiveData<Int> = _entradasCount

    private val _salidasCount = MutableLiveData<Int>().apply {
        value = 0
    }
    val salidasCount: LiveData<Int> = _salidasCount

    private val _repartoActual = MutableLiveData<String?>().apply {
        value = null
    }
    val repartoActual: LiveData<String?> = _repartoActual

    fun updateEntradasCount(count: Int) {
        _entradasCount.value = count
    }

    fun updateSalidasCount(count: Int) {
        _salidasCount.value = count
    }

    fun updateRepartoActual(reparto: String?) {
        _repartoActual.value = reparto
    }
}