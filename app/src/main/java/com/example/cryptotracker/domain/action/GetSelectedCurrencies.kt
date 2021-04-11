package com.example.cryptotracker.domain.action

import com.example.cryptotracker.domain.model.CryptoCurrency
import com.example.cryptotracker.domain.repository.CryptoRepository
import com.example.cryptotracker.domain.service.CryptoService

class GetSelectedCurrencies(private val cryptoService: CryptoService, private val cryptoRepository: CryptoRepository) {

    suspend operator fun invoke(): List<CryptoCurrency> {
        val currencyNames = cryptoRepository.findNames()

        return cryptoService.getSelectedCurrencies(currencyNames.toList())
    }
}