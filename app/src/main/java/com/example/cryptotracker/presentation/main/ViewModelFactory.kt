package com.example.cryptotracker.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptotracker.infrastructure.factory.ActionFactory.createGetCurrencyTypeSelected
import com.example.cryptotracker.infrastructure.factory.ActionFactory.createGetSelectedCurrencies
import com.example.cryptotracker.infrastructure.factory.ActionFactory.createGetTouristDollarPrice
import com.example.cryptotracker.infrastructure.factory.ActionFactory.createSelectCurrencyType
import com.example.cryptotracker.presentation.main.ViewModel as MainViewModel

internal class ViewModelFactory(private val applicationContext: Context): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(
            createGetTouristDollarPrice(),
            createGetSelectedCurrencies(applicationContext),
            createSelectCurrencyType(applicationContext),
            createGetCurrencyTypeSelected(applicationContext)
        ) as T
    }
}