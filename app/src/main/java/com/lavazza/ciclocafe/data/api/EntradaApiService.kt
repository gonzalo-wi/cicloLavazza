package com.lavazza.ciclocafe.data.api

import com.lavazza.ciclocafe.data.model.EntradaRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EntradaApiService {

    @POST("products/in")
    suspend fun sendEntrada(@Body request: EntradaRequest): Response<EntradaResponse>
}
