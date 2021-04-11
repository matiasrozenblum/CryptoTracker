package com.example.cryptotracker.infrastructure.factory

import com.example.cryptotracker.infrastructure.service.ApiCryptoService
import com.example.cryptotracker.infrastructure.service.ApiDollarService

internal object ServiceFactory {

    fun createCryptoService() = ApiCryptoService()

    fun createDollarService() = ApiDollarService()

}