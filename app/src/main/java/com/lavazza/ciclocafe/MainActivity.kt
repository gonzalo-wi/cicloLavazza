package com.lavazza.ciclocafe

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.lavazza.ciclocafe.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_entrada, R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Aplicar color rojo al texto de cerrar sesión
        val menu = navView.menu
        val logoutItem = menu.findItem(R.id.nav_logout)
        val spannable = SpannableString(logoutItem.title)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.logout_red)),
            0,
            spannable.length,
            0
        )
        logoutItem.title = spannable

        // Manejar el clic en cerrar sesión
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    showLogoutDialog()
                    drawerLayout.closeDrawers()
                    true
                }
                else -> {
                    // Comportamiento por defecto para otros items
                    menuItem.onNavDestinationSelected(navController) || onSupportNavigateUp()
                    drawerLayout.closeDrawers()
                    true
                }
            }
        }

        // Ocultar drawer y toolbar en login y reparto
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_login, R.id.nav_reparto -> {
                    supportActionBar?.hide()
                    binding.appBarMain.fab.hide()
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    supportActionBar?.show()
                    binding.appBarMain.fab.show()
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Está seguro que desea cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                logout()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun logout() {
        // Navegar de vuelta al login y limpiar el back stack
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.nav_login)

        // Mostrar mensaje
        Snackbar.make(binding.root, "Sesión cerrada correctamente", Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

// Extension function para navegar
fun MenuItem.onNavDestinationSelected(navController: androidx.navigation.NavController): Boolean {
    return try {
        navController.navigate(itemId)
        true
    } catch (e: Exception) {
        false
    }
}
