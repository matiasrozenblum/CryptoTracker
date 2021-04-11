package com.example.cryptotracker.presentation.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.R
import com.example.cryptotracker.domain.model.DataList
import com.example.cryptotracker.infrastructure.api.CryptoCurrencyApi
import kotlinx.android.synthetic.main.activity_main.cryptoRecyclerView


class AddCryptoActivity : AppCompatActivity() {

    private lateinit var adapter: Adapter

    private val viewModel by viewModels<ViewModel> { ViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_crypto)
        configureViews()
        viewModel.allCurrencies.observe(this, {
            adapter.updateData(it)
        })

        viewModel.load()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchViewItem: MenuItem = menu.findItem(R.id.cryptoSearch)
        configureSearch(searchViewItem)
        return super.onCreateOptionsMenu(menu)
    }

    private fun configureViews() {
        cryptoRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = Adapter(this)
        cryptoRecyclerView.adapter = adapter
    }

    private fun configureSearch(searchViewItem: MenuItem) {
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
    }
}