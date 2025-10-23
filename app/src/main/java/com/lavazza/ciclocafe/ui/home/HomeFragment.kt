package com.lavazza.ciclocafe.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lavazza.ciclocafe.R
import com.lavazza.ciclocafe.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Configurar listeners para las cards
        binding.cardEntrada.setOnClickListener {
            val bundle = Bundle().apply {
                putString("tipo_operacion", "entrada")
            }
            findNavController().navigate(R.id.nav_reparto, bundle)
        }

        binding.cardSalida.setOnClickListener {
            val bundle = Bundle().apply {
                putString("tipo_operacion", "salida")
            }
            findNavController().navigate(R.id.nav_reparto, bundle)
        }

        // Observar cambios en las estadísticas
        homeViewModel.entradasCount.observe(viewLifecycleOwner) { count ->
            binding.tvEntradasCount.text = count.toString()
        }

        homeViewModel.salidasCount.observe(viewLifecycleOwner) { count ->
            binding.tvSalidasCount.text = count.toString()
        }

        homeViewModel.repartoActual.observe(viewLifecycleOwner) { reparto ->
            binding.tvRepartoActual.text = reparto ?: "Sin reparto activo"
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
