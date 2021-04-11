package com.example.cryptotracker.domain.action

import com.example.cryptotracker.domain.model.CurrencyType
import com.example.cryptotracker.domain.repository.CurrencyTypeRepository

class SelectCurrencyType(private val currencyTypeRepository: CurrencyTypeRepository) {
    operator fun invoke(currencyType: CurrencyType){
        currencyTypeRepository.update(currencyType)
    }
}