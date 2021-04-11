package com.example.cryptotracker.domain.action

import com.example.cryptotracker.domain.model.CryptoName
import com.example.cryptotracker.domain.model.Investment
import com.example.cryptotracker.domain.repository.InvestmentRepository

class GetInvestmentForCurrency(private val investmentRepository: InvestmentRepository) {
    operator fun invoke(cryptoName: CryptoName): Investment? {
        return investmentRepository.find(cryptoName)
    }
}