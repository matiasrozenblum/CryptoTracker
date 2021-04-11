package com.example.cryptotracker.infrastructure.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.cryptotracker.domain.model.CryptoName
import com.example.cryptotracker.domain.model.Investment
import com.example.cryptotracker.domain.repository.InvestmentRepository

internal class SharedPreferencesInvestmentRepository(
        private val sharedPreferences: SharedPreferences
) : InvestmentRepository {

    override fun add(investment: Investment) {
        sharedPreferences.edit {
            putLong("$INVESTMENT_PREFIX${investment.cryptoName.value}", investment.amount)
            putFloat("$QUANTITY_PREFIX${investment.cryptoName.value}", investment.cryptoQuantity)
        }
    }

    override fun find(cryptoName: CryptoName): Investment? {
        val amountInvested = sharedPreferences.getLong("$INVESTMENT_PREFIX${cryptoName.value}", 0)
        val cryptoQuantity = sharedPreferences.getFloat("$QUANTITY_PREFIX${cryptoName.value}", 0f)

        if (amountInvested > 0 || cryptoQuantity > 0) {
            return Investment(cryptoName, amountInvested, cryptoQuantity)
        }

        return null
    }

    companion object {
        const val INVESTMENT_PREFIX = "investment_"
        const val QUANTITY_PREFIX = "quantity_"
    }

}