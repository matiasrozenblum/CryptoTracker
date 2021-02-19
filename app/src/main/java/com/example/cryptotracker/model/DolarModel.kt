package com.example.cryptotracker.model

import com.google.gson.annotations.SerializedName

data class Casa(
        val compra: String,
        val venta: String,
        val agencia: String,
        val nombre: String,
        val variacion: String,
        val ventaCero: String,
        val decimales: String
)

data class Item(
        @SerializedName("casa") val casa: Casa
)