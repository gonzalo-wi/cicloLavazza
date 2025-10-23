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
import com.lavazza.ciclocafe.utils.Constants

class RepartoFragment : Fragment() {

    private lateinit var editNumeroReparto: EditText
    private lateinit var btnContinuar: Button
    private lateinit var repartoViewModel: RepartoViewModel
    private var tipoOperacion: String = "entrada" // Por defecto entrada

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_reparto, container, false)

        repartoViewModel = ViewModelProvider(requireActivity()).get(RepartoViewModel::class.java)

        // Obtener el argumento del tipo de operación
        tipoOperacion = arguments?.getString("tipo_operacion") ?: "entrada"

        editNumeroReparto = root.findViewById(R.id.editNumeroReparto)
        btnContinuar = root.findViewById(R.id.btnContinuar)

        btnContinuar.setOnClickListener {
            val numeroReparto = editNumeroReparto.text.toString().trim()

            if (numeroReparto.isEmpty()) {
                editNumeroReparto.error = "Obligatorio"
                Toast.makeText(context, "Por favor ingrese el número de reparto", Toast.LENGTH_SHORT).show()
            } else if (!Constants.Repartos.esRepartoValido(numeroReparto)) {
                editNumeroReparto.error = "Solo repartos ${Constants.Repartos.REPARTO_502} o ${Constants.Repartos.REPARTO_503}"
                Toast.makeText(context, "Solo se permiten los repartos ${Constants.Repartos.REPARTO_502} y ${Constants.Repartos.REPARTO_503}", Toast.LENGTH_LONG).show()
            } else {
                repartoViewModel.setNumeroReparto(numeroReparto)

                // Navegar según el tipo de operación
                if (tipoOperacion == "salida") {
                    findNavController().navigate(R.id.action_repartoFragment_to_salidaFragment)
                } else {
                    findNavController().navigate(R.id.action_repartoFragment_to_entradaFragment)
                }
            }
        }

        return root
    }
}
