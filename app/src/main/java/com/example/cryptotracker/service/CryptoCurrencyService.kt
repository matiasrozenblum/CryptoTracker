package com.example.cryptotracker.service

import com.example.cryptotracker.model.Body
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CryptoCurrencyService {
    @GET("cryptocurrency/quotes/latest")
    fun getCryptoCurrency(@Query("symbol") symbol: String, @Header("X-CMC_PRO_API_KEY") header: String) :Call<Body>
}