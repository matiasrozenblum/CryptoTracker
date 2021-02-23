package com.example.cryptotracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
        getDollarPrice()
    }

    fun getCoins() {
        /**
         *  enqueue ensures that this call does not occur on the Main UI thread,
         *  network transactions should be done off the Main UI Thread
         */
        val cryptoService = CryptoCurrencyService.create()
        cryptoService.getCryptoCurrency("btc,eth,eos,trx","8f6819ab-b836-43e6-8635-c10ca600265e").enqueue(object : retrofit2.Callback<Body> {
            override fun onResponse(
                    call: retrofit2.Call<Body>,
                    response: retrofit2.Response<Body>
            ) {
                println("body: " + response.body())
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
                println("body: " + response.body())
                val turista = response.body()?.get(6)
                if (turista != null) {
                    val format: NumberFormat = NumberFormat.getInstance(Locale.FRANCE)
                    dollarPrice = format.parse(turista.casa.venta)!!.toDouble()
                    println("PRECIO: $dollarPrice")
                    getCoins()
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Item>>, t: Throwable) {
                println("Failed $t")
            }

        })
    }
}