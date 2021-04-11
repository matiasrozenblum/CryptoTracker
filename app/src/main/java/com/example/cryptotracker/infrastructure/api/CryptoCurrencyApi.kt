package com.example.cryptotracker.infrastructure.api

import com.example.cryptotracker.domain.model.Body
import com.example.cryptotracker.domain.model.DataList
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CryptoCurrencyApi {
    @GET("cryptocurrency/quotes/latest")
    fun getCryptoCurrency(
        @Query("symbol") symbol: String,
        @Header("X-CMC_PRO_API_KEY") header: String
    ): Call<Body>

    @GET("cryptocurrency/listings/latest")
    fun getCryptoCurrencies(
        @Query("limit") limit: Int,
        @Header("X-CMC_PRO_API_KEY") header: String
    ): Call<DataList>

    companion object Factory {

        private const val API_URL = "https://pro-api.coinmarketcap.com/v1/"

        fun create(): CryptoCurrencyApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_URL)
                .build()

            return retrofit.create(CryptoCurrencyApi::class.java);
        }
    }
}