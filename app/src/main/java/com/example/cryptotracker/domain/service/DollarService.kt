package com.example.cryptotracker.domain.service

interface DollarService {
    suspend fun getTouristPrice(): Double?
}