package com.example.cryptotracker.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.MainActivity
import com.example.cryptotracker.R
import com.example.cryptotracker.model.CryptoModel
import com.example.cryptotracker.utils.Constants
import com.example.cryptotracker.utils.getTwoDigitsDouble
import com.example.cryptotracker.utils.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.crypto_layout.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


class CryptoAdapter(private val activity: MainActivity) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {
    private var cryptoCoins: List<CryptoModel> = Collections.emptyList()
    private var cryptoQuantity: Float = 0.0f
    private var investment: Double = 0.0
    private var actualInvestment: Double = 0.0

    override fun getItemCount(): Int = cryptoCoins.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder = CryptoViewHolder(parent.inflate(R.layout.crypto_layout))

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val coin = cryptoCoins[position]

        holder.apply {
            coinName.text = coin.name
            coinSymbol.text = coin.symbol
            coinPrice.text = getTwoDigitsDouble(coin.quote.usd.price)
            oneHourChange.text = "${getTwoDigitsDouble(coin.quote.usd.percent_change_1h)}%"
            twentyFourHourChange.text = "${getTwoDigitsDouble(coin.quote.usd.percent_change_24h)}%"
            sevenDayChange.text = "${getTwoDigitsDouble(coin.quote.usd.percent_change_7d)}%"

            val sharedPref =  activity.getSharedPreferences("crypto_tracker", Context.MODE_PRIVATE) ?: return
            investment = Double.fromBits(sharedPref.getLong("investment_${coin.name}", 0))
            cryptoQuantity =  sharedPref.getFloat("quantity_${coin.name}", 0f)
            var price = cryptoQuantity * coin.quote.usd.price.toDouble()
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

            Picasso.with(itemView.context).load(Constants.imageUrl + coin.symbol.toLowerCase() + ".png").into(coinIcon)

            applyColor(oneHourChange, coin.quote.usd.percent_change_1h)

            applyColor(twentyFourHourChange, coin.quote.usd.percent_change_24h)

            applyColor(sevenDayChange, coin.quote.usd.percent_change_7d)
        }
        holder.itemView.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it.context)
            builder.setTitle("Cálculo de Inversion")

            val lila1 = LinearLayout(it.context)
            lila1.orientation = LinearLayout.VERTICAL
            val investmentInput = EditText(it.context)
            val quantityInput = EditText(it.context)
            investmentInput.hint = "Ingrese monto invertido"
            quantityInput.hint = "Ingrese cantidad de cripto"
            investmentInput.inputType = InputType.TYPE_CLASS_NUMBER.or(InputType.TYPE_NUMBER_FLAG_DECIMAL)
            quantityInput.inputType = InputType.TYPE_CLASS_NUMBER.or(InputType.TYPE_NUMBER_FLAG_DECIMAL)
            lila1.addView(investmentInput)
            lila1.addView(quantityInput)
            builder.setView(lila1)
            builder.setPositiveButton("OK") { _, _ ->
                run {
                    investment = investmentInput.text.toString().toDouble()
                    cryptoQuantity = quantityInput.text.toString().toFloat()
                    val sharedPref = activity.getSharedPreferences("crypto_tracker", Context.MODE_PRIVATE)
                    var price = cryptoQuantity * coin.quote.usd.price.toDouble()
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
                        putLong("investment_${coin.name}", investment.toRawBits())
                        putFloat("quantity_${coin.name}", cryptoQuantity)
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
                    val coins = sharedPref.getStringSet("crypto_coins", mutableSetOf("btc", "eth")) as MutableSet<String>
                    coins.remove(coin.symbol)
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
        var coinName = view.coinName
        var coinSymbol = view.coinSymbol
        var coinPrice = view.priceUsdText
        var oneHourChange = view.percentChange1hText
        var twentyFourHourChange = view.percentChange24hText
        var sevenDayChange = view.percentChange7dayText
        var coinIcon = view.coinIcon
        var initial = view.initial
        var actual = view.actual
        var dollarSign = view.dollarSign3
    }

    fun updateData(cryptoCoins: Map<String,CryptoModel>) {
        updateData(cryptoCoins.mapNotNull { it.value })
    }

    private fun updateData(cryptoCoins: List<CryptoModel>) {
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


