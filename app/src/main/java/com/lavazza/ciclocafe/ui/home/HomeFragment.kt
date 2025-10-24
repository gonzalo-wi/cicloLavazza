package com.lavazza.ciclocafe.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lavazza.ciclocafe.R
import com.lavazza.ciclocafe.databinding.FragmentHomeBinding
import com.lavazza.ciclocafe.utils.NetworkUtils
import com.lavazza.ciclocafe.utils.SessionManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        sessionManager = SessionManager(requireContext())

        // Mostrar saludo personalizado con el nombre del usuario
        val userName = sessionManager.getUserName()
        binding.tvFechaActual.text = "Hola, $userName"

        // Configurar listeners para las cards
        binding.cardEntrada.setOnClickListener {
            checkNetworkAndNavigate("entrada")
        }

        binding.cardSalida.setOnClickListener {
            checkNetworkAndNavigate("salida")
        }

        // Observar cambios en las estadísticas
        homeViewModel.entradasCount.observe(viewLifecycleOwner) { count ->
            binding.tvEntradasCount.text = count.toString()
            actualizarResumen()
        }

        homeViewModel.salidasCount.observe(viewLifecycleOwner) { count ->
            binding.tvSalidasCount.text = count.toString()
            actualizarResumen()
        }

        return root
    }

    override fun onResume() {
        super.onResume()
        actualizarResumen()
    }

    private fun checkNetworkAndNavigate(tipoOperacion: String) {
        if (!NetworkUtils.isConnectedToWifi(requireContext())) {
            Toast.makeText(
                context,
                "⚠️ No está conectado a WiFi. Conéctese a la red de la empresa para continuar.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val bundle = Bundle().apply {
                putString("tipo_operacion", tipoOperacion)
            }
            findNavController().navigate(R.id.nav_reparto, bundle)
        }
    }

    private fun actualizarResumen() {
        val entradas = homeViewModel.entradasCount.value ?: 0
        val salidas = homeViewModel.salidasCount.value ?: 0
        val total = entradas + salidas

        binding.tvResumenDia.text = when {
            total == 0 -> "Sin operaciones"
            total == 1 -> "1 operación registrada"
            else -> "$total operaciones registradas"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

