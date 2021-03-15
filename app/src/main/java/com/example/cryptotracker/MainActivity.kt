package com.example.cryptotracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.adapter.CryptoAdapter
import com.example.cryptotracker.model.Body
import com.example.cryptotracker.model.Item
import com.example.cryptotracker.service.CryptoCurrencyService
import com.example.cryptotracker.service.DollarService
import kotlinx.android.synthetic.main.activity_main.*
import java.text.NumberFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: CryptoAdapter
    var dollarPrice = 0.0
    var coins = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cryptoRecyclerView.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
        )
        adapter = CryptoAdapter(this)
        cryptoRecyclerView.adapter = adapter
        val sharedPref =  this.applicationContext.getSharedPreferences("crypto_tracker", Context.MODE_PRIVATE) ?: return
        coins = sharedPref.getStringSet("crypto_coins", mutableSetOf("btc", "eth")) as MutableSet<String>
        getDollarPrice()

        fab.setOnClickListener {
            val intent = Intent(this, AddCryptoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val checkable: MenuItem = menu.findItem(R.id.currency)
        val switch = MenuItemCompat.getActionView(checkable) as Switch
        val sharedPref =  this.applicationContext.getSharedPreferences("crypto_tracker", Context.MODE_PRIVATE)
        switch.isChecked = sharedPref.getBoolean("currency", false)
        switch.text = if(switch.isChecked) "ARS" else "USD"
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPref.edit {
                putBoolean("currency", isChecked)
                apply()
            }
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
            true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    fun getCoins() {
        val cryptoService = CryptoCurrencyService.create()
        val coinString = coins.joinToString(separator = ",") { it }
        cryptoService.getCryptoCurrency(coinString, "8f6819ab-b836-43e6-8635-c10ca600265e").enqueue(object : retrofit2.Callback<Body> {
            override fun onResponse(
                    call: retrofit2.Call<Body>,
                    response: retrofit2.Response<Body>
            ) {
                runOnUiThread {
                    response.body()?.let { adapter.updateData(it.data) }
                }
            }

            override fun onFailure(call: retrofit2.Call<Body>, t: Throwable) {
                println("Failed $t")
            }

        })
    }

    private fun getDollarPrice() {
        val dollarService = DollarService.create()
        dollarService.getDollarPrice("valoresprincipales").enqueue(object : retrofit2.Callback<List<Item>> {
            override fun onResponse(
                    call: retrofit2.Call<List<Item>>,
                    response: retrofit2.Response<List<Item>>
            ) {
                val turista = response.body()?.get(6)
                if (turista != null) {
                    val format: NumberFormat = NumberFormat.getInstance(Locale.FRANCE)
                    dollarPrice = format.parse(turista.casa.venta)!!.toDouble()
                    getCoins()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Item>>, t: Throwable) {
                println("Failed $t")
            }

        })
    }

    override fun onRestart() {
        super.onRestart()
        val refresh = Intent(this, MainActivity::class.java)
        startActivity(refresh);
        finish()
    }
}