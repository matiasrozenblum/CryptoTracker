package com.example.cryptotracker.infrastructure.factory

import android.content.Context
import com.example.cryptotracker.infrastructure.repository.SharedPreferencesCryptoRepository
import com.example.cryptotracker.infrastructure.repository.SharedPreferencesCurrencyTypeRepository

internal object Repositories {

    private const val SHARED_PREFERENCES_NAME = "crypto_tracker"

    fun createCurrencyTypeRepository(context: Context) =
        SharedPreferencesCurrencyTypeRepository(createSharedPreferences(context))

    fun createCryptoRepository(context: Context) =
        SharedPreferencesCryptoRepository(createSharedPreferences(context))

    private fun createSharedPreferences(applicationContext: Context) =
        applicationContext.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
}