package com.lavazza.ciclocafe.utils

/**
 * Application constants
 */
object Constants {

    /** Repartos available for Entrada/Salida selection */
    object Repartos {
        const val REPARTO_502 = "502"
        const val REPARTO_503 = "503"
        const val REPARTO_505 = "505"

        // Agregamos 505 a la lista de válidos
        val REPARTOS_DISPONIBLES = listOf(REPARTO_502, REPARTO_503, REPARTO_505)

        fun esRepartoValido(numeroReparto: String): Boolean =
            numeroReparto in REPARTOS_DISPONIBLES
    }

    /** Generic login credentials (in-memory) */
    object Auth {
        const val USERNAME = "Cafe"
        const val PASSWORD = "Cafe123"
    }
}
