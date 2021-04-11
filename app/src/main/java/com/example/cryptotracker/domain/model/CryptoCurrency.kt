package com.example.cryptotracker.domain.model

import com.google.gson.annotations.SerializedName

data class CryptoCurrency(
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
    val data: Map<String, CryptoCurrency>
)

data class DataList(
    val data: List<CryptoCurrency>
)