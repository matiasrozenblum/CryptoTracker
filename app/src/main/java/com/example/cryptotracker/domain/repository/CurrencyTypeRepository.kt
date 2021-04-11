package com.example.cryptotracker.domain.repository

import com.example.cryptotracker.domain.model.CurrencyType

interface CurrencyTypeRepository {
    fun find(): CurrencyType
    fun update(currencyType: CurrencyType)
}