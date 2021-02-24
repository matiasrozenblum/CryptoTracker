package com.example.cryptotracker.service

import com.example.cryptotracker.model.Body
import com.example.cryptotracker.model.DataList
import com.example.cryptotracker.utils.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CryptoCurrencyService {
    @GET("cryptocurrency/quotes/latest")
    fun getCryptoCurrency(@Query("symbol") symbol: String, @Header("X-CMC_PRO_API_KEY") header: String) :Call<Body>

    @GET("cryptocurrency/listings/latest")
    fun getCryptoCurrencies(@Header("X-CMC_PRO_API_KEY") header: String) :Call<DataList>

    companion object Factory {

        fun create(): CryptoCurrencyService {

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.apiUrl)
                    .build()

            return retrofit.create(CryptoCurrencyService::class.java);

        }
    }
}