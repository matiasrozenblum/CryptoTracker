package com.example.cryptotracker.infrastructure.api

import com.example.cryptotracker.domain.model.Item
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface DollarApi {

    @GET("api.php")
    fun getDollarPrice(@Query("type") type: String): Call<List<Item>>

    companion object Factory {

        private const val API_URL = "https://www.dolarsi.com/api/"

        fun create(): DollarApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_URL)
                .build()

            return retrofit.create(DollarApi::class.java);
        }
    }
}