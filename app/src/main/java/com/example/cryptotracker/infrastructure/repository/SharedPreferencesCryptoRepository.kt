package com.example.cryptotracker.infrastructure.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.cryptotracker.domain.model.CryptoName
import com.example.cryptotracker.domain.repository.CryptoRepository

internal class SharedPreferencesCryptoRepository(
    private val sharedPreferences: SharedPreferences
): CryptoRepository {

    override fun findNames(): Set<CryptoName> {
        val names = sharedPreferences.getStringSet(CRYPTO_NAMES_KEY, DEFAULT_NAME_VALUES) ?: setOf()

        return names.mapTo(HashSet()) { CryptoName(it) }
    }

    override fun addNew(name: CryptoName) {
        val allNames = findNames().toMutableSet()
        allNames.add(name)

        sharedPreferences.edit {
            putStringSet(CRYPTO_NAMES_KEY, allNames.mapTo(HashSet()){it.value})
        }
    }

    private companion object {
        val DEFAULT_NAME_VALUES = setOf("BTC", "ETH")
        const val CRYPTO_NAMES_KEY = "crypto_coins"
    }
}