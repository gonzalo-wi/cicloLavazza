package com.lavazza.ciclocafe.data.api

import com.lavazza.ciclocafe.data.model.OutRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SalidaApiService {

    @POST("products/out")
    suspend fun sendSalida(@Body request: OutRequest): Response<SalidaResponse>
}
