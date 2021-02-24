package com.example.cryptotracker

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.adapter.CryptoAdapter
import com.example.cryptotracker.model.Body
import com.example.cryptotracker.model.Item
import com.example.cryptotracker.service.CryptoCurrencyService
import com.example.cryptotracker.service.DollarService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_crypto_activity.*
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

        fab.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it.context)
            val inflater = this.layoutInflater
            val dialogView: View = inflater.inflate(R.layout.add_crypto_activity, null)
            builder.setView(dialogView)
            builder.setPositiveButton("OK") { dialog, which -> dialog.cancel() }
            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            builder.show()
        }
    }

    fun getCoins() {
        val cryptoService = CryptoCurrencyService.create()
        cryptoService.getCryptoCurrency("btc,eth,eos,trx", "8f6819ab-b836-43e6-8635-c10ca600265e").enqueue(object : retrofit2.Callback<Body> {
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
}