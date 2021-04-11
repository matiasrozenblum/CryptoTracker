package com.example.cryptotracker.domain.model

import com.google.gson.annotations.SerializedName

data class ExchangeRate(
        @SerializedName("venta") val selling: String
)

data class Item(
        @SerializedName("casa") val exchangeRate: ExchangeRate
)