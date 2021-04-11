package com.example.cryptotracker.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.domain.action.GetCurrencyTypeSelected
import com.example.cryptotracker.domain.action.GetSelectedCurrencies
import com.example.cryptotracker.domain.action.GetTouristDollarPrice
import com.example.cryptotracker.domain.action.SelectCurrencyType
import com.example.cryptotracker.domain.model.CryptoCurrency
import com.example.cryptotracker.domain.model.CurrencyType
import com.example.cryptotracker.domain.model.CurrencyType.ARS
import com.example.cryptotracker.domain.model.CurrencyType.USD
import com.example.cryptotracker.domain.model.Quote
import kotlinx.coroutines.launch

internal class ViewModel(
        private val getTouristDollarPrice: GetTouristDollarPrice,
        private val getSelectedCurrencies: GetSelectedCurrencies,
        private val selectCurrencyType: SelectCurrencyType,
        private val getCurrencyTypeSelected: GetCurrencyTypeSelected
) : ViewModel() {

    val currencies = MutableLiveData<List<CryptoCurrencyViewData>>()
    val dollarPrice = MutableLiveData<Double>()

    fun load() {
        viewModelScope.launch {
            val touristPrice = getTouristDollarPrice()

            if (touristPrice != null) {
                dollarPrice.postValue(touristPrice.value)
                val selectedCurrencies = getSelectedCurrencies()
                currencies.postValue(mapToViewData(selectedCurrencies))
            }
        }
    }

    fun currencyTypeSelected(): String {
        return getCurrencyTypeSelected().name
    }

    fun isCurrencyTypeChecked(): Boolean {
        return getCurrencyTypeSelected() == ARS
    }

    fun updateCurrencyType(isChecked: Boolean) {
        selectCurrencyType(if (isChecked) ARS else USD)
    }

    private fun mapToViewData(currencies: List<CryptoCurrency>) =
            currencies.map {
                CryptoCurrencyViewData(
                        it.id,
                        it.name,
                        it.symbol,
                        it.quote
                )
            }


    data class InvestmentCryptoViewData(
            val crypto: CryptoCurrencyViewData,
            val initialInvestment: Long,
            val actualInvestment: Float
    )

    data class CryptoCurrencyViewData(
            val id: String,
            val name: String,
            val symbol: String,
            val quote: Quote
    )
}