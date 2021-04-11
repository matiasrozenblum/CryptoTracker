package com.example.cryptotracker.infrastructure.service

import com.example.cryptotracker.domain.service.DollarService
import com.example.cryptotracker.infrastructure.api.DollarApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.*

internal class ApiDollarService: DollarService {

    private val api: DollarApi = DollarApi.create()

    override suspend fun getTouristPrice(): Double? {
        return withContext(Dispatchers.IO) {
            val item = api.getDollarPrice(TYPE).execute().body()?.getOrNull(TOURIST_DOLLAR_INDEX) ?: return@withContext null

            return@withContext parseToDouble(item.exchangeRate.selling)
        }
    }


    private fun parseToDouble(price: String): Double {
        val format: NumberFormat = NumberFormat.getInstance(Locale.FRANCE)
        return format.parse(price)!!.toDouble()
    }

    private companion object {
        const val TOURIST_DOLLAR_INDEX = 6
        const val TYPE = "valoresprincipales"
    }
}