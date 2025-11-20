package com.lavazza.ciclocafe.ui.entrada

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lavazza.ciclocafe.R
import com.lavazza.ciclocafe.data.model.Product
import com.lavazza.ciclocafe.data.repository.ProductRepository
import com.lavazza.ciclocafe.ui.reparto.RepartoViewModel
import com.lavazza.ciclocafe.utils.NetworkUtils
import kotlinx.coroutines.launch

class EntradaFragment : Fragment() {

    private lateinit var textNumeroReparto: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnGuardarEntrada: Button
    private lateinit var fabAddRow: View

    private lateinit var repartoViewModel: RepartoViewModel
    private val entradaViewModel: EntradaViewModel by lazy { ViewModelProvider(this)[EntradaViewModel::class.java] }

    private val repository = ProductRepository()
    private var products: List<Product> = emptyList()
    private var adapter: EntradaAdapter? = null
    private val entradaItems = mutableListOf<EntradaItem>()
    private var numeroReparto: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_entrada, container, false)

        textNumeroReparto = root.findViewById(R.id.textNumeroReparto)
        recyclerView = root.findViewById(R.id.recyclerViewEntrada)
        progressBar = root.findViewById(R.id.progressBar)
        btnGuardarEntrada = root.findViewById(R.id.btnGuardarEntrada)
        fabAddRow = root.findViewById(R.id.fabAddRow)

        repartoViewModel = ViewModelProvider(requireActivity()).get(RepartoViewModel::class.java)
        repartoViewModel.numeroReparto.observe(viewLifecycleOwner) { reparto ->
            numeroReparto = reparto
            textNumeroReparto.text = getString(R.string.label_reparto, reparto)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        btnGuardarEntrada.setOnClickListener { showConfirmDialog() }
        fabAddRow.setOnClickListener { addBlankRow() }

        loadProducts()
        return root
    }

    private fun addBlankRow() {
        entradaItems.add(EntradaItem(producto = "", selectedProduct = null))
        adapter?.submitList(entradaItems.toList())
    }

    private fun loadProducts() {
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            repository.getProducts().fold(
                onSuccess = { productList ->
                    progressBar.visibility = View.GONE
                    products = productList
                    prepareInitialRows()
                },
                onFailure = { error ->
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error productos: ${error.message}", Toast.LENGTH_LONG).show()
                    // Fallback mínima
                    products = listOf(
                        Product("K", "CAFETERA", "CAFETERA"),
                        Product("P", "PEDIDO", "Pedido")
                    )
                    prepareInitialRows()
                }
            )
        }
    }

    private fun prepareInitialRows() {
        entradaItems.clear()
        // Una fila por producto existente
        products.forEach { p ->
            entradaItems.add(
                EntradaItem(
                    producto = p.name,
                    selectedProduct = p,
                    vueltaLleno = 0,
                    vueltaTotal = 0,
                    recambio = 0
                )
            )
        }
        adapter = EntradaAdapter(products)
        recyclerView.adapter = adapter
        adapter?.submitList(entradaItems.toList())
    }

    private fun showConfirmDialog() {
        if (!NetworkUtils.isConnectedToWifi(requireContext())) {
            showWifiWarning()
            return
        }

        if (!entradaItems.any { it.vueltaLleno > 0 || it.vueltaTotal > 0 || it.recambio > 0 }) {
            Toast.makeText(requireContext(), "Ingrese algún valor antes de guardar", Toast.LENGTH_SHORT).show()
            return
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmar Entrada")
            .setMessage("¿Guardar entrada del reparto $numeroReparto?")
            .setPositiveButton("Sí, Guardar") { _, _ -> guardarEntrada() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showWifiWarning() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("⚠️ Sin WiFi")
            .setMessage("Conéctese a la red WiFi de la empresa para enviar datos. ¿Abrir configuración WiFi?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Abrir WiFi") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "No se pudo abrir WiFi", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun guardarEntrada() {
        // Placeholder: Aquí iría el POST real para entrada
        progressBar.visibility = View.VISIBLE
        btnGuardarEntrada.isEnabled = false

        // Simulación rápida
        lifecycleScope.launch {
            // Podría construir request similar a OutRequest si hace falta otro modelo
            progressBar.visibility = View.GONE
            btnGuardarEntrada.isEnabled = true
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Éxito")
                .setMessage("Entrada guardada para reparto $numeroReparto")
                .setPositiveButton("OK") { _, _ ->
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                .show()
        }
    }
}

