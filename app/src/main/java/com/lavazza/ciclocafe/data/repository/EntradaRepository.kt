package com.lavazza.ciclocafe.data.repository

import com.lavazza.ciclocafe.data.api.EntradaApiService
import com.lavazza.ciclocafe.data.api.EntradaResponse
import com.lavazza.ciclocafe.data.api.RetrofitClient
import com.lavazza.ciclocafe.data.model.EntradaRequest

class EntradaRepository {

    private val apiService: EntradaApiService = RetrofitClient.createService(EntradaApiService::class.java)

    suspend fun sendEntrada(request: EntradaRequest): Result<EntradaResponse> {
        return try {
            val response = apiService.sendEntrada(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
