package com.example.cryptotracker.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptotracker.infrastructure.factory.ActionFactory
import com.example.cryptotracker.presentation.add.ViewModel as AddCryptoViewModel

internal class ViewModelFactory: ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddCryptoViewModel(
            ActionFactory.createGetAllCurrencies()
        ) as T
    }
}