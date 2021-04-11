package com.example.cryptotracker.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotracker.R
import com.example.cryptotracker.presentation.add.AddCryptoActivity
import kotlinx.android.synthetic.main.activity_main.*


internal class MainActivity : AppCompatActivity(), CryptoUpdatesListener {

    private lateinit var adapter: Adapter
    var dollarPrice = 0.0

    private val viewModel by viewModels<ViewModel> { ViewModelFactory(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureViews()

        viewModel.dollarPrice.observe(this, {
            this.dollarPrice = it
        })

        viewModel.currencies.observe(this, {
            this.adapter.updateData(it)
        })

        viewModel.load()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val checkable: MenuItem = menu.findItem(R.id.currency)
        updateSwitch(checkable)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun update(cryptoCurrencyViewData: ViewModel.CryptoCurrencyViewData, investmentData: CryptoUpdatesListener.InvestmentData) {

    }

    override fun delete(cryptoCurrencyViewData: ViewModel.CryptoCurrencyViewData) {

    }

    private fun configureViews() {
        cryptoRecyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = Adapter(this)
        cryptoRecyclerView.adapter = adapter
        fab.setOnClickListener {
            val intent = Intent(this, AddCryptoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateSwitch(checkable: MenuItem) {
        with(MenuItemCompat.getActionView(checkable) as Switch) {
            isChecked = viewModel.isCurrencyTypeChecked()
            text = viewModel.currencyTypeSelected()
            setOnCheckedChangeListener { _, isChecked ->
                viewModel.updateCurrencyType(isChecked)
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }
    }


    override fun onRestart() {
        super.onRestart()
        val refresh = Intent(this, MainActivity::class.java)
        startActivity(refresh);
        finish()
    }
}