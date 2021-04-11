package com.example.cryptotracker.infrastructure.service

import com.example.cryptotracker.domain.model.CryptoCurrency
import com.example.cryptotracker.domain.model.CryptoName
import com.example.cryptotracker.domain.service.CryptoService
import com.example.cryptotracker.infrastructure.api.CryptoCurrencyApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ApiCryptoService: CryptoService {

    private val api: CryptoCurrencyApi = CryptoCurrencyApi.create()

    override suspend fun getAllCurrencies(): List<CryptoCurrency> {
        return withContext(Dispatchers.IO) {
            val result = api.getCryptoCurrencies(CURRENCIES_LIMIT, API_KEY).execute()

            result.body()!!.data
        }
    }

    override suspend fun getSelectedCurrencies(names: List<CryptoName>): List<CryptoCurrency> {
        val symbols = extractSymbolsFromCryptoNames(names)
        return withContext(Dispatchers.IO) {
            val result = api.getCryptoCurrency(symbols, API_KEY).execute()

            result.body()!!.data.values.toList()
        }
    }

    private fun extractSymbolsFromCryptoNames(names: List<CryptoName>) =
        names.map { it.value }.joinToString(separator = ",") { it }


    private companion object {
        const val CURRENCIES_LIMIT = 5000
        const val API_KEY = "8f6819ab-b836-43e6-8635-c10ca600265e"
    }
}