package com.example.cryptotracker.domain.repository

import com.example.cryptotracker.domain.model.CryptoName

interface CryptoRepository {
    fun findNames(): Set<CryptoName>
    fun addNew(name: CryptoName)
}