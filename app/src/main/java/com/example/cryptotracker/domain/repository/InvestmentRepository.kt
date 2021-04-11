package com.example.cryptotracker.domain.repository

import com.example.cryptotracker.domain.model.CryptoName
import com.example.cryptotracker.domain.model.Investment

interface InvestmentRepository {
    fun add(investment: Investment)
    fun find(cryptoName: CryptoName): Investment?
}