package com.example.cryptotracker.domain.action

import com.example.cryptotracker.domain.model.CryptoCurrency
import com.example.cryptotracker.domain.service.CryptoService

class GetAllCurrencies(private val cryptoService: CryptoService) {

    suspend operator fun invoke(): List<CryptoCurrency> {
        return cryptoService.getAllCurrencies()
    }
}