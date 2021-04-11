package com.example.cryptotracker.presentation.main

internal interface CryptoUpdatesListener {
    fun update(cryptoCurrencyViewData: ViewModel.CryptoCurrencyViewData, investmentData: InvestmentData)
    fun delete(cryptoCurrencyViewData: ViewModel.CryptoCurrencyViewData)

    data class InvestmentData(val investment: Double, val cryptoQuantity: Float)
}