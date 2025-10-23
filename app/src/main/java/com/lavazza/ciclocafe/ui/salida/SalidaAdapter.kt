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

    inner class SalidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textProducto: TextView = itemView.findViewById(R.id.textProducto)
        private val editTotal: EditText = itemView.findViewById(R.id.editTotal)

        private var watcherTotal: TextWatcher? = null

        fun bind(item: SalidaItem) {
            textProducto.text = item.producto

            // Remove previous watcher to avoid recursive triggers during setText
            watcherTotal?.let { editTotal.removeTextChangedListener(it) }

            // Show empty when 0 for better UX
            val textValue = if (item.total == 0) "" else item.total.toString()
            if (editTotal.text.toString() != textValue) {
                editTotal.setText(textValue)
            }

            watcherTotal = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    val value = s?.toString()?.toIntOrNull() ?: 0
                    if (value != item.total) {
                        item.total = value
                    }
                }
            }
            editTotal.addTextChangedListener(watcherTotal)
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
