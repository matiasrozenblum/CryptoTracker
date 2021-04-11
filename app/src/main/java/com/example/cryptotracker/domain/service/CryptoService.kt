package com.example.cryptotracker.domain.service

import com.example.cryptotracker.domain.model.CryptoCurrency
import com.example.cryptotracker.domain.model.CryptoName

interface CryptoService {
    suspend fun getAllCurrencies(): List<CryptoCurrency>
    suspend fun getSelectedCurrencies(names: List<CryptoName>): List<CryptoCurrency>
}