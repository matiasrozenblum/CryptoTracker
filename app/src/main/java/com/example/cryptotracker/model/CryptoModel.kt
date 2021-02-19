package com.example.cryptotracker.model

import com.google.gson.annotations.SerializedName

data class CryptoModel(
        val id: String,
        val name: String,
        val symbol: String,
        val quote: Quote
)

data class Quote(
    @SerializedName("USD") val usd: Usd
)

data class Usd(
    val price: String,
    val percent_change_1h: String,
    val percent_change_24h: String,
    val percent_change_7d: String
)

data class Body(
    val data: List<CryptoModel>
)