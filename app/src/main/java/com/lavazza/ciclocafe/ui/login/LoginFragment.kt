package com.lavazza.ciclocafe.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lavazza.ciclocafe.R
import com.lavazza.ciclocafe.ui.common.LoadingDialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_login, container, false)

        editUsername = root.findViewById(R.id.editUsername)
        editPassword = root.findViewById(R.id.editPassword)
        btnLogin = root.findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = editUsername.text.toString()
            val password = editPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                performLogin(username, password)
            }
        }

        return root
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
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



