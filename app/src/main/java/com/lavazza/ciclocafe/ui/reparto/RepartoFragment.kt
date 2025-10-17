package com.lavazza.ciclocafe.ui.reparto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lavazza.ciclocafe.R

class RepartoFragment : Fragment() {

    private lateinit var editNumeroReparto: EditText
    private lateinit var btnContinuar: Button
    private lateinit var repartoViewModel: RepartoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_reparto, container, false)

        repartoViewModel = ViewModelProvider(requireActivity()).get(RepartoViewModel::class.java)

        editNumeroReparto = root.findViewById(R.id.editNumeroReparto)
        btnContinuar = root.findViewById(R.id.btnContinuar)

        btnContinuar.setOnClickListener {
            val numeroReparto = editNumeroReparto.text.toString()

            if (numeroReparto.isEmpty()) {
                Toast.makeText(context, "Por favor ingrese el número de reparto", Toast.LENGTH_SHORT).show()
            } else {
                repartoViewModel.setNumeroReparto(numeroReparto)
                findNavController().navigate(R.id.action_repartoFragment_to_entradaFragment)
            }
        }

        return root
    }
}

