package com.lavazza.ciclocafe.data.repository

import com.lavazza.ciclocafe.data.api.SalidaApiService
import com.lavazza.ciclocafe.data.api.SalidaResponse
import com.lavazza.ciclocafe.data.api.RetrofitClient
import com.lavazza.ciclocafe.data.model.SalidaRequest

class SalidaRepository {

    private val apiService: SalidaApiService = RetrofitClient.createService(SalidaApiService::class.java)

    suspend fun sendSalida(request: SalidaRequest): Result<SalidaResponse> {
        return try {
            val response = apiService.sendSalida(request)
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

