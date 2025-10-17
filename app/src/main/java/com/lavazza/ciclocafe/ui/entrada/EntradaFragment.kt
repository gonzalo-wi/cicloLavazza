package com.lavazza.ciclocafe.ui.entrada

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lavazza.ciclocafe.R

class EntradaFragment : Fragment() {

    private lateinit var textNumeroReparto: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAddRow: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_entrada, container, false)

        textNumeroReparto = root.findViewById(R.id.textNumeroReparto)
        recyclerView = root.findViewById(R.id.recyclerViewEntrada)
        fabAddRow = root.findViewById(R.id.fabAddRow)

        // Mostrar número de reparto si se pasó como argumento (opcional)
        val reparto = arguments?.getString("numero_reparto")
        if (!reparto.isNullOrBlank()) {
            textNumeroReparto.text = getString(R.string.reparto_subtitle) + " " + reparto
        }

        // Setup básico del RecyclerView (vacío por ahora)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val v = LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
                return object : RecyclerView.ViewHolder(v) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                // placeholder
                (holder.itemView as? TextView)?.text = ""
            }

            override fun getItemCount(): Int = 0
        }

        fabAddRow.setOnClickListener {
            Toast.makeText(requireContext(), "Agregar fila (pendiente implementar)", Toast.LENGTH_SHORT).show()
        }

        return root
    }
}
