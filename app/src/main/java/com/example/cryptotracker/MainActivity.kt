package com.example.cryptotracker

import android.icu.text.Normalizer.NO
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.adapter.CryptoAdapter
import com.example.cryptotracker.model.Body
import com.example.cryptotracker.model.Item
import com.example.cryptotracker.service.CryptoCurrencyService
import com.example.cryptotracker.service.DollarService
import com.example.cryptotracker.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.NumberFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    /**
     * Declare variables
     */
    private lateinit var adapter: CryptoAdapter
    var dollarPrice = 0.0

    /**
     * holds the REST endpoint to query,
     * with the “limit=10” parameter passed.
     * This will list the top 10 coins.
     */
    private val apiUrl = Constants.apiUrl

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
        var builder: OkHttpClient.Builder? = OkHttpClient().newBuilder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        builder!!.addInterceptor(interceptor)
        val client = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(CryptoCurrencyService::class.java)

        service.getCryptoCurrency("btc,eth,eos,trx","8f6819ab-b836-43e6-8635-c10ca600265e").enqueue(object : retrofit2.Callback<Body> {
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

    fun getDollarPrice() {
        val builder: OkHttpClient.Builder? = OkHttpClient().newBuilder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        builder!!.addInterceptor(interceptor)
        val client = builder.build()
        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.dolarApiUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(DollarService::class.java)

        service.getDollarPrice("valoresprincipales").enqueue(object : retrofit2.Callback<List<Item>> {
            override fun onResponse(
                    call: retrofit2.Call<List<Item>>,
                    response: retrofit2.Response<List<Item>>
            ) {
                println("body: " + response.body())
                val turista = response.body()?.get(4)
                if (turista != null) {
                    val format: NumberFormat = NumberFormat.getInstance(Locale.FRANCE)
                    dollarPrice = format.parse(turista.casa.venta).toDouble()
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