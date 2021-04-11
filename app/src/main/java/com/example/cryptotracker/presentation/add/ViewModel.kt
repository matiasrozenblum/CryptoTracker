package com.example.cryptotracker.presentation.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.domain.action.AddNewCurrency
import com.example.cryptotracker.domain.action.AddNewInvestment
import com.example.cryptotracker.domain.action.GetAllCurrencies
import com.example.cryptotracker.domain.model.CryptoCurrency
import com.example.cryptotracker.domain.model.CryptoName
import com.example.cryptotracker.domain.model.Investment
import com.example.cryptotracker.domain.model.Quote
import kotlinx.coroutines.launch

internal class ViewModel(
        private val getAllCurrencies: GetAllCurrencies,
        private val addNewInvestment: AddNewInvestment,
        private val addNewCurrency: AddNewCurrency
) : ViewModel() {

    val allCurrencies = MutableLiveData<List<CryptoCurrencyViewData>>()

    fun load() {
        viewModelScope.launch {
            kotlin.runCatching { getAllCurrencies() }
                    .onSuccess { allCurrencies.postValue(mapToViewData(it)) }
                    .onFailure { Log.d("AddCryptoViewModel", "Error getting all currencies", it) }
        }
    }

    fun addNewCrypto(crypto: CryptoCurrencyViewData, investment: Double, cryptoQuantity: Float) {
        addNewCurrency(CryptoName(crypto.symbol))
        addNewInvestment(createInvestment(crypto.symbol, investment, cryptoQuantity))
    }

    private fun createInvestment(cryptoSymbol: String, investment: Double, cryptoQuantity: Float) =
            Investment(CryptoName(cryptoSymbol), investment.toRawBits(), cryptoQuantity)

    private fun mapToViewData(currencies: List<CryptoCurrency>) =
            currencies.map {
                CryptoCurrencyViewData(
                        it.id,
                        it.name,
                        it.symbol,
                        it.quote
                )
            }

    data class CryptoCurrencyViewData(
            val id: String,
            val name: String,
            val symbol: String,
            val quote: Quote
    )

}