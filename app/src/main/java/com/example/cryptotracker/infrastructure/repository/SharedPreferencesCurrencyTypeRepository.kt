package com.example.cryptotracker.infrastructure.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.cryptotracker.domain.model.CurrencyType
import com.example.cryptotracker.domain.model.CurrencyType.USD
import com.example.cryptotracker.domain.repository.CurrencyTypeRepository

internal class SharedPreferencesCurrencyTypeRepository(
    private val sharedPreferences: SharedPreferences
): CurrencyTypeRepository {

    override fun find(): CurrencyType {
        val type = sharedPreferences.getString(CURRENCY_TYPE_KEY, USD.name) ?: USD.name
        return CurrencyType.valueOf(type)
    }

    override fun update(currencyType: CurrencyType) {
        sharedPreferences.edit {
            putString(CURRENCY_TYPE_KEY, currencyType.name)
        }
    }

    private companion object {
        const val CURRENCY_TYPE_KEY = "currency_type"
    }
}