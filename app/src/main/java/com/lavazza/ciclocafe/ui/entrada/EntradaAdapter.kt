package com.lavazza.ciclocafe.ui.entrada

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lavazza.ciclocafe.R

class EntradaAdapter : ListAdapter<EntradaItem, EntradaAdapter.EntradaViewHolder>(EntradaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntradaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entrada_row, parent, false)
        return EntradaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntradaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EntradaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val editProducto: EditText = itemView.findViewById(R.id.editProducto)
        private val editVueltaLleno: EditText = itemView.findViewById(R.id.editVueltaLleno)
        private val editVueltaTotal: EditText = itemView.findViewById(R.id.editVueltaTotal)
        private val textCambio: TextView = itemView.findViewById(R.id.textCambio)

        private var currentItem: EntradaItem? = null

        fun bind(item: EntradaItem) {
            currentItem = item

            // Set values without triggering listeners
            editProducto.setText(item.producto)
            editVueltaLleno.setText(if (item.vueltaLleno == 0) "" else item.vueltaLleno.toString())
            editVueltaTotal.setText(if (item.vueltaTotal == 0) "" else item.vueltaTotal.toString())
            updateCambio()

            // Setup listeners
            editProducto.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    currentItem?.producto = s.toString()
                }
            })

            editVueltaLleno.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    currentItem?.vueltaLleno = s.toString().toIntOrNull() ?: 0
                    updateCambio()
                }
            })

            editVueltaTotal.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    currentItem?.vueltaTotal = s.toString().toIntOrNull() ?: 0
                    updateCambio()
                }
            })
        }

        private fun updateCambio() {
            currentItem?.let {
                textCambio.text = it.cambio.toString()
            }
        }
    }

    class EntradaDiffCallback : DiffUtil.ItemCallback<EntradaItem>() {
        override fun areItemsTheSame(oldItem: EntradaItem, newItem: EntradaItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: EntradaItem, newItem: EntradaItem): Boolean {
            return oldItem == newItem
        }
    }
}

