package com.example.cryptotracker.presentation.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptotracker.domain.action.GetAllCurrencies
import com.example.cryptotracker.domain.model.CryptoCurrency
import com.example.cryptotracker.domain.model.Quote
import kotlinx.coroutines.launch

internal class ViewModel(private val getAllCurrencies: GetAllCurrencies) : ViewModel() {

    val allCurrencies = MutableLiveData<List<CryptoCurrencyViewData>>()

    fun load() {
        viewModelScope.launch {
            kotlin.runCatching { getAllCurrencies() }
                .onSuccess { allCurrencies.postValue(mapToViewData(it)) }
                .onFailure { Log.d("AddCryptoViewModel", "Error getting all currencies", it) }
        }
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

    data class CryptoCurrencyViewData(
        val id: String,
        val name: String,
        val symbol: String,
        val quote: Quote
    )

}