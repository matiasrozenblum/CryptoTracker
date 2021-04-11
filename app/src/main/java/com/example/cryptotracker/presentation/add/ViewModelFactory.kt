package com.example.cryptotracker.presentation.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptotracker.infrastructure.factory.ActionFactory.createAddNewCurrency
import com.example.cryptotracker.infrastructure.factory.ActionFactory.createGetAllCurrencies
import com.example.cryptotracker.infrastructure.factory.ActionFactory.createNewInvestment
import com.example.cryptotracker.presentation.add.ViewModel as AddCryptoViewModel

internal class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddCryptoViewModel(
                createGetAllCurrencies(),
                createNewInvestment(context),
                createAddNewCurrency(context)
        ) as T
    }
}