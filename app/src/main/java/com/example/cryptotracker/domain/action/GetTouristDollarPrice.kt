package com.example.cryptotracker.domain.action

import com.example.cryptotracker.domain.model.DollarPrice
import com.example.cryptotracker.domain.service.DollarService

class GetTouristDollarPrice(private val dollarService: DollarService) {
    suspend operator fun invoke(): DollarPrice? {
        return dollarService.getTouristPrice()?.let { DollarPrice(it) }
    }
}