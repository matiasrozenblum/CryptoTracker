package com.example.cryptotracker.domain.action

import com.example.cryptotracker.domain.model.Investment
import com.example.cryptotracker.domain.repository.InvestmentRepository

class AddNewInvestment(private val investmentRepository: InvestmentRepository) {
    operator fun invoke(investment: Investment){
        investmentRepository.add(investment)
    }
}