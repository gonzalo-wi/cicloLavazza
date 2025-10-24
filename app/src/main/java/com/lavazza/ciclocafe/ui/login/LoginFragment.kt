package com.lavazza.ciclocafe.ui.login

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lavazza.ciclocafe.R
import com.lavazza.ciclocafe.ui.common.LoadingDialogFragment
import com.lavazza.ciclocafe.utils.SessionManager
import com.lavazza.ciclocafe.utils.NetworkUtils
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        sessionManager = SessionManager(requireContext())

        editUsername = root.findViewById(R.id.editUsername)
        editPassword = root.findViewById(R.id.editPassword)
        btnLogin = root.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = editUsername.text.toString()
            val password = editPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                checkNetworkAndLogin(username, password)
            }
        }

        return root
    }

    private fun checkNetworkAndLogin(username: String, password: String) {
        val connectionType = NetworkUtils.getConnectionType(requireContext())

        when {
            !NetworkUtils.hasInternetConnection(requireContext()) -> {
                showNetworkWarning(
                    "Sin Conexión a Internet",
                    "No hay conexión a internet disponible. Por favor, conéctese a la red WiFi de la empresa para continuar.",
                    showWifiSettings = true
                )
            }
            NetworkUtils.isConnectedToMobileData(requireContext()) -> {
                showNetworkWarning(
                    "Conexión mediante Datos Móviles",
                    "Está usando datos móviles. Para usar esta aplicación debe conectarse a la red WiFi de la empresa.\n\n¿Desea continuar de todas formas?",
                    allowContinue = true,
                    showWifiSettings = true,
                    onContinue = { performLogin(username, password) }
                )
            }
            NetworkUtils.isConnectedToWifi(requireContext()) -> {
                // Está conectado a WiFi, continuar normalmente
                performLogin(username, password)
            }
            else -> {
                showNetworkWarning(
                    "Conexión Desconocida",
                    "No se pudo detectar el tipo de conexión. Por favor, verifique que está conectado a la red WiFi de la empresa.",
                    showWifiSettings = true
                )
            }
        }
    }

    private fun showNetworkWarning(
        title: String,
        message: String,
        allowContinue: Boolean = false,
        showWifiSettings: Boolean = false,
        onContinue: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .apply {
                if (allowContinue) {
                    setPositiveButton("Continuar de todas formas") { _, _ ->
                        onContinue?.invoke()
                    }
                    if (showWifiSettings) {
                        setNeutralButton("Abrir WiFi") { _, _ ->
                            openWifiSettings()
                        }
                    }
                    setNegativeButton("Cancelar", null)
                } else {
                    setPositiveButton("Entendido", null)
                    if (showWifiSettings) {
                        setNegativeButton("Abrir WiFi") { _, _ ->
                            openWifiSettings()
                        }
                    }
                }
            }
            .show()
    }

    private fun openWifiSettings() {
        try {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivity(intent)
            Toast.makeText(context, "Conéctese al WiFi y vuelva a intentar", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, "No se pudo abrir la configuración de WiFi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogin(username: String, password: String) {
        // Mostrar indicador de carga
        val loadingDialog = LoadingDialogFragment.newInstance("Iniciando sesión...")
        loadingDialog.show(parentFragmentManager, LoadingDialogFragment.TAG)

        // Deshabilitar el botón de login mientras se procesa
        btnLogin.isEnabled = false

        lifecycleScope.launch {
            // Simular autenticación con delay
            val isValid = withContext(Dispatchers.IO) {
                delay(1500) // Simula tiempo de carga (1.5 segundos)

                // Credenciales temporales de desarrollo: Gon / gon
                (username.equals("cafe", ignoreCase = true) && password == "cafe123") ||
                        (username.equals("Gon", ignoreCase = true) && password == "gon")
            }

            // Cerrar el diálogo de carga
            loadingDialog.dismissAllowingStateLoss()
            btnLogin.isEnabled = true

            if (isValid) {
                // Guardar sesión del usuario
                val userName = username.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }
                sessionManager.saveUserSession(userName)

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
