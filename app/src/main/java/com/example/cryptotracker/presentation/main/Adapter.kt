package com.example.cryptotracker.presentation.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.R
import com.example.cryptotracker.presentation.Constants
import com.example.cryptotracker.presentation.getTwoDigitsDouble
import com.example.cryptotracker.presentation.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.crypto_layout.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


internal class Adapter(private val activity: MainActivity) : RecyclerView.Adapter<Adapter.CryptoViewHolder>() {

    private var cryptoCoins: List<ViewModel.CryptoCurrencyViewData> = Collections.emptyList()
    private var cryptoQuantity: Float = 0.0f
    private var investment: Double = 0.0
    private var actualInvestment: Double = 0.0

    override fun getItemCount(): Int = cryptoCoins.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder = CryptoViewHolder(parent.inflate(R.layout.crypto_layout))

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val currency = cryptoCoins[position]

        holder.bindData(currency)

        holder.apply {
            val sharedPref =  activity.getSharedPreferences("crypto_tracker", Context.MODE_PRIVATE) ?: return
            investment = Double.fromBits(sharedPref.getLong("investment_${currency.symbol}", 0))
            cryptoQuantity =  sharedPref.getFloat("quantity_${currency.symbol}", 0f)
            var price = cryptoQuantity * currency.quote.usd.price.toDouble()
            val currencyMode = sharedPref.getBoolean("currency", false)
            if (currencyMode) {
                price *= activity.dollarPrice
            } else {
                investment /= activity.dollarPrice
            }
            actualInvestment = BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN).toDouble()

            holder.actual.text = actualInvestment.toString()
            holder.initial.text = BigDecimal(investment).setScale(2, RoundingMode.HALF_EVEN).toDouble().toString()

            applyWinningsColor(holder)

            Picasso.with(itemView.context).load(Constants.imageUrl + currency.symbol.toLowerCase() + ".png").into(coinIcon)

            applyColor(oneHourChange, currency.quote.usd.percent_change_1h)

            applyColor(twentyFourHourChange, currency.quote.usd.percent_change_24h)

            applyColor(sevenDayChange, currency.quote.usd.percent_change_7d)
        }
        holder.itemView.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it.context)
            builder.setTitle("Cálculo de Inversion")

            val lila1 = LinearLayout(it.context)
            lila1.orientation = LinearLayout.VERTICAL
            val investmentTextView = TextView(it.context)
            val quantityTextView = TextView(it.context)
            investmentTextView.text = activity.getString(R.string.investmentTV)
            quantityTextView.text = activity.getString(R.string.quantityTV)
            val investmentInput = EditText(it.context)
            val quantityInput = EditText(it.context)
            val sharedPref =  activity.getSharedPreferences("crypto_tracker", Context.MODE_PRIVATE)
            investment = Double.fromBits(sharedPref.getLong("investment_${currency.symbol}", 0))
            cryptoQuantity =  sharedPref.getFloat("quantity_${currency.symbol}", 0f)
            investmentInput.hint = "Ingrese monto invertido"
            quantityInput.hint = "Ingrese cantidad de cripto"
            if (investment > 0.0) {
                investmentInput.setText(investment.toString())
                quantityInput.setText(cryptoQuantity.toString())
            }
            investmentInput.inputType = InputType.TYPE_CLASS_NUMBER.or(InputType.TYPE_NUMBER_FLAG_DECIMAL)
            quantityInput.inputType = InputType.TYPE_CLASS_NUMBER.or(InputType.TYPE_NUMBER_FLAG_DECIMAL)
            lila1.addView(investmentTextView)
            lila1.addView(investmentInput)
            lila1.addView(quantityTextView)
            lila1.addView(quantityInput)
            builder.setView(lila1)
            builder.setPositiveButton("OK") { _, _ ->
                run {
                    investment = investmentInput.text.toString().toDouble()
                    cryptoQuantity = quantityInput.text.toString().toFloat()
                    val sharedPref = activity.getSharedPreferences("crypto_tracker", Context.MODE_PRIVATE)
                    var price = cryptoQuantity * currency.quote.usd.price.toDouble()
                    val currencyMode = sharedPref.getBoolean("currency", false)
                    if (currencyMode) {
                        price *= activity.dollarPrice
                    } else {
                        investment /= activity.dollarPrice
                    }
                    actualInvestment = BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN).toDouble()
                    holder.initial.text = BigDecimal(investment).setScale(2, RoundingMode.HALF_EVEN).toDouble().toString()
                    holder.actual.text = actualInvestment.toString()
                    applyWinningsColor(holder)
                    with (sharedPref.edit()) {
                        putLong("investment_${currency.symbol}", investment.toRawBits())
                        putFloat("quantity_${currency.symbol}", cryptoQuantity)
                        apply()
                    }
                }
            }

            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            builder.show()
        }

        holder.itemView.setOnLongClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it.context)
            builder.setTitle("¿Borrar cripto?")
            val lila1 = LinearLayout(it.context)
            lila1.orientation = LinearLayout.VERTICAL
            builder.setView(lila1)
            builder.setPositiveButton("OK") { _, _ ->
                run {
                    val sharedPref = activity.applicationContext.getSharedPreferences("crypto_tracker", Context.MODE_PRIVATE)
                    val coins = sharedPref.getStringSet("crypto_coins", mutableSetOf("BTC", "ETH")) as MutableSet<String>
                    coins.remove(currency.symbol)
                    sharedPref.edit {
                        remove("crypto_coins")
                    }
                    sharedPref.edit {
                        putStringSet("crypto_coins", coins)
                    }
                    activity.recreate()
                }
            }

            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            builder.show()
            true
        }
    }

    class CryptoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val coinName: TextView = view.coinName
        private val coinSymbol: TextView = view.coinSymbol
        private val coinPrice: TextView = view.priceUsdText
        val oneHourChange: TextView = view.percentChange1hText
        val twentyFourHourChange: TextView = view.percentChange24hText
        val sevenDayChange: TextView = view.percentChange7dayText
        val coinIcon: ImageView = view.coinIcon
        val initial: TextView = view.initial
        val actual: TextView = view.actual
        val dollarSign: TextView = view.dollarSign3

        fun bindData(currency: ViewModel.CryptoCurrencyViewData){
            coinName.text = currency.name
            coinSymbol.text = currency.symbol
            coinPrice.text = getTwoDigitsDouble(currency.quote.usd.price)
            oneHourChange.text = "${getTwoDigitsDouble(currency.quote.usd.percent_change_1h)}%"
            twentyFourHourChange.text = "${getTwoDigitsDouble(currency.quote.usd.percent_change_24h)}%"
            sevenDayChange.text = "${getTwoDigitsDouble(currency.quote.usd.percent_change_7d)}%"
        }
    }

    fun updateData(cryptoCoins: List<ViewModel.CryptoCurrencyViewData>) {
        this.cryptoCoins = cryptoCoins
        notifyDataSetChanged()
    }

    private fun applyColor(textView: TextView, text: String) {
        textView.setTextColor(activity.resources.getColor(when {
            text.contains("-") -> R.color.red
            else -> R.color.green
        }))
    }

    private fun applyWinningsColor(holder: CryptoViewHolder) {
        if (actualInvestment > investment) {
            holder.actual.setTextColor(activity.resources.getColor(R.color.green))
            holder.dollarSign.setTextColor(activity.resources.getColor(R.color.green))
        } else if (actualInvestment < investment) {
            holder.actual.setTextColor(activity.resources.getColor(R.color.red))
            holder.dollarSign.setTextColor(activity.resources.getColor(R.color.red))
        }
    }
}


