package com.example.cryptotracker.service

import com.example.cryptotracker.model.Casa
import com.example.cryptotracker.model.Item
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DollarService {
    @GET("api.php")
    fun getDollarPrice(@Query("type") type: String) : Call<List<Item>>
}