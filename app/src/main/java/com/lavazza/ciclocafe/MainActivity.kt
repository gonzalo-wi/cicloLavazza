package com.lavazza.ciclocafe

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_entrada,
                R.id.nav_salida,
                R.id.nav_reparto
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNav, navController)

        bottomNav.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_logout -> {
                    mostrarDialogoCerrarSesion()
                    // Devolvemos false para no cambiar la selección si el usuario cancela
                    false
                }
                R.id.nav_entrada -> {
                    val args = bundleOf("tipo_operacion" to "entrada")
                    navController.navigate(R.id.nav_reparto, args)
                    true
                }
                R.id.nav_salida -> {
                    val args = bundleOf("tipo_operacion" to "salida")
                    navController.navigate(R.id.nav_reparto, args)
                    true
                }
                else -> NavigationUI.onNavDestinationSelected(item, navController)
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.isVisible = when (destination.id) {
                R.id.nav_home, R.id.nav_entrada, R.id.nav_salida, R.id.nav_reparto -> true
                else -> false
            }
        }
    }

    private fun mostrarDialogoCerrarSesion() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Querés cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                val navOptions = androidx.navigation.navOptions {
                    popUpTo(R.id.mobile_navigation) { inclusive = true }
                }
                navController.navigate(R.id.nav_login, null, navOptions)
            }
            .setNegativeButton("No", null)
            .show()
    }

    // Remove top app bar menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
