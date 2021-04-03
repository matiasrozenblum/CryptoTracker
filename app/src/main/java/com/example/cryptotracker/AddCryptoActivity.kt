package com.example.cryptotracker

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.adapter.AddCryptoAdapter
import com.example.cryptotracker.model.DataList
import com.example.cryptotracker.service.CryptoCurrencyService
import kotlinx.android.synthetic.main.activity_add_crypto.*
import kotlinx.android.synthetic.main.activity_main.cryptoRecyclerView


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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
            val searchViewItem: MenuItem = menu.findItem(R.id.cryptoSearch)
        val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun getAllCoins() {
        val cryptoService = CryptoCurrencyService.create()
        cryptoService.getCryptoCurrencies(5000, "8f6819ab-b836-43e6-8635-c10ca600265e").enqueue(object : retrofit2.Callback<DataList> {
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