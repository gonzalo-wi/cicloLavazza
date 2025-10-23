package com.lavazza.ciclocafe.ui.salida

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
import com.lavazza.ciclocafe.data.model.Product

class SalidaAdapter(
    private val products: List<Product>
) : ListAdapter<SalidaItem, SalidaAdapter.SalidaViewHolder>(SalidaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalidaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_salida_row, parent, false)
        return SalidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: SalidaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SalidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textProducto: TextView = itemView.findViewById(R.id.textProducto)
        private val editVueltaLleno: EditText = itemView.findViewById(R.id.editVueltaLleno)
        private val editVueltaTotal: EditText = itemView.findViewById(R.id.editVueltaTotal)
        private val editRecambio: EditText = itemView.findViewById(R.id.editRecambio)

        private var currentItem: SalidaItem? = null

        private var watcherLleno: TextWatcher? = null
        private var watcherTotal: TextWatcher? = null
        private var watcherRecambio: TextWatcher? = null

        fun bind(item: SalidaItem) {
            currentItem = item

            textProducto.text = item.producto

            // Remove previous watchers to avoid duplicate triggers on recycling
            watcherLleno?.let { editVueltaLleno.removeTextChangedListener(it) }
            watcherTotal?.let { editVueltaTotal.removeTextChangedListener(it) }
            watcherRecambio?.let { editRecambio.removeTextChangedListener(it) }

            // Set values without triggering watchers
            editVueltaLleno.setText(if (item.vueltaLleno == 0) "" else item.vueltaLleno.toString())
            editVueltaTotal.setText(if (item.vueltaTotal == 0) "" else item.vueltaTotal.toString())
            editRecambio.setText(if (item.recambio == 0) "" else item.recambio.toString())

            watcherLleno = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    currentItem?.vueltaLleno = s?.toString()?.toIntOrNull() ?: 0
                }
            }
            editVueltaLleno.addTextChangedListener(watcherLleno)

            watcherTotal = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    currentItem?.vueltaTotal = s?.toString()?.toIntOrNull() ?: 0
                }
            }
            editVueltaTotal.addTextChangedListener(watcherTotal)

            watcherRecambio = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    currentItem?.recambio = s?.toString()?.toIntOrNull() ?: 0
                }
            }
            editRecambio.addTextChangedListener(watcherRecambio)
        }
    }

    class SalidaDiffCallback : DiffUtil.ItemCallback<SalidaItem>() {
        override fun areItemsTheSame(oldItem: SalidaItem, newItem: SalidaItem): Boolean {
            return oldItem.selectedProduct?.idProducto == newItem.selectedProduct?.idProducto
        }

        override fun areContentsTheSame(oldItem: SalidaItem, newItem: SalidaItem): Boolean {
            return oldItem == newItem
        }
    }
}

