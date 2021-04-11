package com.example.cryptotracker.infrastructure.factory

import android.content.Context
import com.example.cryptotracker.domain.action.*

object ActionFactory {

    fun createGetAllCurrencies() = GetAllCurrencies(ServiceFactory.createCryptoService())

    fun createGetSelectedCurrencies(context: Context) =
        GetSelectedCurrencies(ServiceFactory.createCryptoService(), Repositories.createCryptoRepository(context))

    fun createGetTouristDollarPrice() = GetTouristDollarPrice(ServiceFactory.createDollarService())

    fun createGetCurrencyTypeSelected(context: Context) = GetCurrencyTypeSelected(Repositories.createCurrencyTypeRepository(context))

    fun createSelectCurrencyType(context: Context) = SelectCurrencyType(Repositories.createCurrencyTypeRepository(context))

}