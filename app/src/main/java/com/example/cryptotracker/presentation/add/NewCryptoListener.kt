package com.example.cryptotracker.presentation.add

internal interface NewCryptoListener {
    fun onNewCryptoAdded(crypto: ViewModel.CryptoCurrencyViewData, investmentData: InvestmentData)


    data class InvestmentData(val investment: Double, val cryptoQuantity: Float)
}