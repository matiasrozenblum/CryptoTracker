package com.example.cryptotracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.adapter.AddCryptoAdapter
import com.example.cryptotracker.adapter.CryptoAdapter
import com.example.cryptotracker.model.DataList
import com.example.cryptotracker.service.CryptoCurrencyService
import kotlinx.android.synthetic.main.activity_main.*

class AddCryptoActivity : AppCompatActivity() {
    private lateinit var adapter: AddCryptoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_crypto)
        cryptoRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = AddCryptoAdapter(this)
        cryptoRecyclerView.adapter = adapter
        getAllCoins()
    }

    private fun getAllCoins() {
        val cryptoService = CryptoCurrencyService.create()
        cryptoService.getCryptoCurrencies("8f6819ab-b836-43e6-8635-c10ca600265e").enqueue(object : retrofit2.Callback<DataList> {
            override fun onResponse(
                call: retrofit2.Call<DataList>,
                response: retrofit2.Response<DataList>
            ) {
                response.body()?.let { adapter.updateData(it.data) }
            }

            override fun onFailure(call: retrofit2.Call<DataList>, t: Throwable) {
                println("Failed $t")
            }

        })
    }
}