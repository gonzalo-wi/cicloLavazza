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
        binding.cardIniciarReparto.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_repartoFragment)
        }

        binding.cardEntrada.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_repartoFragment)
        }

        binding.cardSalida.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_repartoFragment)
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
