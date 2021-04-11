package com.example.cryptotracker.domain.action

import com.example.cryptotracker.domain.model.CryptoName
import com.example.cryptotracker.domain.repository.CryptoRepository

class AddNewCurrency(private val cryptoRepository: CryptoRepository) {
    operator fun invoke(name: CryptoName){
        cryptoRepository.addNew(name)
    }
}