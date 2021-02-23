package com.example.cryptotracker.service

import com.example.cryptotracker.model.Item
import com.example.cryptotracker.utils.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface DollarService {
    @GET("api.php")
    fun getDollarPrice(@Query("type") type: String) : Call<List<Item>>

    companion object Factory {

        fun create(): DollarService {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.dolarApiUrl)
                    .build()

            return retrofit.create(DollarService::class.java);

        }

    }
}