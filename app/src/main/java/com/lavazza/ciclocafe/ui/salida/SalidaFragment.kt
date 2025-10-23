package com.lavazza.ciclocafe.ui.salida

import android.os.Bundle
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
import com.lavazza.ciclocafe.data.model.SalidaRequest
import com.lavazza.ciclocafe.data.model.Product
import com.lavazza.ciclocafe.data.model.ProductExit
import com.lavazza.ciclocafe.data.repository.SalidaRepository
import com.lavazza.ciclocafe.data.repository.ProductRepository
import com.lavazza.ciclocafe.ui.reparto.RepartoViewModel
import kotlinx.coroutines.launch

class SalidaFragment : Fragment() {

    private lateinit var textNumeroReparto: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var btnGuardarSalida: Button
    private lateinit var repartoViewModel: RepartoViewModel

    private val repository = ProductRepository()
    private val salidaRepository = SalidaRepository()

    private var products: List<Product> = emptyList()
    private var adapter: SalidaAdapter? = null
    private val salidaItems = mutableListOf<SalidaItem>()
    private var numeroReparto: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_salida, container, false)

        textNumeroReparto = root.findViewById(R.id.textNumeroReparto)
        recyclerView = root.findViewById(R.id.recyclerViewSalida)
        progressBar = root.findViewById(R.id.progressBar)
        btnGuardarSalida = root.findViewById(R.id.btnGuardarSalida)

        // ViewModel para número de reparto
        repartoViewModel = ViewModelProvider(requireActivity()).get(RepartoViewModel::class.java)
        repartoViewModel.numeroReparto.observe(viewLifecycleOwner) { reparto ->
            numeroReparto = reparto
            textNumeroReparto.text = "Reparto: $reparto"
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        btnGuardarSalida.setOnClickListener { showConfirmDialog() }

        loadProducts()

        return root
    }

    private fun loadProducts() {
        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            repository.getProducts().fold(
                onSuccess = { productList ->
                    progressBar.visibility = View.GONE
                    products = productList

                    // Crear un item por cada producto
                    salidaItems.clear()
                    for (product in products) {
                        salidaItems.add(
                            SalidaItem(
                                producto = product.name,
                                selectedProduct = product,
                                vueltaLleno = 0,
                                vueltaTotal = 0,
                                recambio = 0
                            )
                        )
                    }

                    adapter = SalidaAdapter(products)
                    recyclerView.adapter = adapter
                    adapter?.submitList(salidaItems.toList())

                    Toast.makeText(requireContext(), "Productos cargados: ${products.size}", Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error al cargar productos: ${error.message}", Toast.LENGTH_LONG).show()

                    // Respaldo: productos mínimos
                    products = listOf(
                        Product("K", "CAFETERA", "CAFETERA"),
                        Product("P", "PEDIDO", "Pedido")
                    )

                    salidaItems.clear()
                    for (product in products) {
                        salidaItems.add(
                            SalidaItem(
                                producto = product.name,
                                selectedProduct = product,
                                vueltaLleno = 0,
                                vueltaTotal = 0,
                                recambio = 0
                            )
                        )
                    }

                    adapter = SalidaAdapter(products)
                    recyclerView.adapter = adapter
                    adapter?.submitList(salidaItems.toList())
                }
            )
        }
    }

    private fun showConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmar Salida")
            .setMessage("¿Está seguro que desea guardar la salida del reparto $numeroReparto?")
            .setPositiveButton("Sí, Guardar") { _, _ -> enviarSalida() }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun enviarSalida() {
        val hasData = salidaItems.any { it.vueltaTotal > 0 || it.vueltaLleno > 0 || it.recambio > 0 }
        if (!hasData) {
            Toast.makeText(requireContext(), "Por favor ingrese al menos un dato", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        btnGuardarSalida.isEnabled = false

        lifecycleScope.launch {
            val request = getSalidaData()

            salidaRepository.sendSalida(request).fold(
                onSuccess = {
                    progressBar.visibility = View.GONE
                    btnGuardarSalida.isEnabled = true

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Éxito")
                        .setMessage("Salida guardada correctamente para el reparto $numeroReparto")
                        .setPositiveButton("OK") { _, _ ->
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                        .show()
                },
                onFailure = { error ->
                    progressBar.visibility = View.GONE
                    btnGuardarSalida.isEnabled = true

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Error")
                        .setMessage("Error al guardar salida: ${error.message}")
                        .setPositiveButton("OK", null)
                        .show()
                }
            )
        }
    }

    private fun getSalidaData(): SalidaRequest {
        val productList = mutableListOf<ProductExit>()

        salidaItems.forEach { item ->
            item.selectedProduct?.let { product ->
                productList.add(
                    ProductExit(
                        idProducto = product.idProducto,
                        vta_total = item.vueltaTotal,
                        vta_lleno = item.vueltaLleno,
                        recambio = item.recambio
                    )
                )
            }
        }

        return SalidaRequest(
            products = productList,
            idReparto = numeroReparto.toIntOrNull() ?: 0
        )
    }
}

