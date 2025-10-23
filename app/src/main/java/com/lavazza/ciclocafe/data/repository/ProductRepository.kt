package com.lavazza.ciclocafe.data.repository

import com.lavazza.ciclocafe.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ProductRepository {

    private val baseUrl = "http://192.168.0.12/api/cafe"

    suspend fun getProducts(): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$baseUrl/products")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonObject = JSONObject(response)

                val success = jsonObject.getBoolean("success")
                if (success) {
                    val dataArray = jsonObject.getJSONArray("data")
                    val products = mutableListOf<Product>()

                    for (i in 0 until dataArray.length()) {
                        val productJson = dataArray.getJSONObject(i)
                        products.add(
                            Product(
                                idProducto = productJson.getString("idProducto"),
                                name = productJson.getString("name"),
                                pack = productJson.getString("pack")
                            )
                        )
                    }

                    Result.success(products)
                } else {
                    Result.failure(Exception("API returned success=false"))
                }
            } else {
                Result.failure(Exception("HTTP error: $responseCode"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

