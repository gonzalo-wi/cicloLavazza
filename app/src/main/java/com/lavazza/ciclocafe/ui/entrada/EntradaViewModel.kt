package com.lavazza.ciclocafe.ui.entrada

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EntradaViewModel : ViewModel() {

    private val _entradas = MutableLiveData<MutableList<EntradaItem>>().apply {
        value = mutableListOf()
    }
    val entradas: LiveData<MutableList<EntradaItem>> = _entradas

    fun addNewRow() {
        val currentList = _entradas.value ?: mutableListOf()
        currentList.add(EntradaItem())
        _entradas.value = currentList
    }

    fun updateItem(position: Int, item: EntradaItem) {
        val currentList = _entradas.value ?: return
        if (position < currentList.size) {
            currentList[position] = item
            _entradas.value = currentList
        }
    }
}
