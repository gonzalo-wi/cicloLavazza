package com.lavazza.ciclocafe.data.api

import com.lavazza.ciclocafe.data.model.SalidaRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SalidaApiService {

    @POST("products/out")
    suspend fun sendSalida(@Body request: SalidaRequest): Response<SalidaResponse>
}

