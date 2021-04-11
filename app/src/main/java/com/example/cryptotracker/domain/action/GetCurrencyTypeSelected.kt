package com.example.cryptotracker.domain.action

import com.example.cryptotracker.domain.model.CurrencyType
import com.example.cryptotracker.domain.repository.CurrencyTypeRepository

class GetCurrencyTypeSelected(private val currencyTypeRepository: CurrencyTypeRepository) {
    operator fun invoke(): CurrencyType {
        return currencyTypeRepository.find()
    }
}