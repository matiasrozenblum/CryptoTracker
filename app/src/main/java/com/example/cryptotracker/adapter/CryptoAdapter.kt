package com.example.cryptotracker.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.MainActivity
import com.example.cryptotracker.R
import com.example.cryptotracker.model.CryptoModel
import com.example.cryptotracker.model.CryptoType
import com.example.cryptotracker.model.Data
import com.example.cryptotracker.utils.Constants
import com.example.cryptotracker.utils.getTwoDigitsDouble
import com.example.cryptotracker.utils.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.crypto_layout.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import kotlin.reflect.full.declaredMemberProperties


/**
 * Created by CharlesAE on 2/25/18.
 */
class CryptoAdapter(activity: MainActivity) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {
    private var cryptoCoins: List<CryptoModel> = Collections.emptyList()
    private var cryptoQuantity: Float = 0.0f
    private var investment: Double = 0.0
    private var actualInvestment: Double = 0.0
    private val activity = activity

    /**
     *  lets the Adapter know how many items to display
     */
    override fun getItemCount(): Int = cryptoCoins.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder = CryptoViewHolder(parent.inflate(R.layout.crypto_layout))

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        /**
         *  coin - A single CryptoModel object from list
         */
        val coin = cryptoCoins[position]
        /**
         * get CryptoModel data and bind them to corresponding
         * viewholder widgets(text view, imageview etc)
         */
        holder.apply {
            coinName.text = coin.name
            coinSymbol.text = coin.symbol
            coinPrice.text = getTwoDigitsDouble(coin.quote.usd.price)
            oneHourChange.text = "${getTwoDigitsDouble(coin.quote.usd.percent_change_1h)}%"
            twentyFourHourChange.text = "${getTwoDigitsDouble(coin.quote.usd.percent_change_24h)}%"
            sevenDayChange.text = "${getTwoDigitsDouble(coin.quote.usd.percent_change_7d)}%"

            val sharedPref =  activity.getSharedPreferences("crypto_tracker", Context.MODE_PRIVATE) ?: return
            investment = Double.fromBits(sharedPref.getLong("investment_${coin.name}", 0))
            Log.d("COIN NAME", "quantity_${coin.name}")
            cryptoQuantity =  sharedPref.getFloat("quantity_${coin.name}", 0f)
            Log.d("CRYPTO CUANTITY", cryptoQuantity.toString())
            Log.d("COIN PRICE", coin.quote.usd.price)
            actualInvestment = BigDecimal(cryptoQuantity * coin.quote.usd.price.toDouble() * activity.dollarPrice).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            holder.actual.text = actualInvestment.toString()
            holder.initial.text = investment.toString()
            if (actualInvestment > investment) {
                holder.actual.setTextColor(Color.parseColor("#32CD32"))
                holder.dollarSign.setTextColor(Color.parseColor("#32CD32"))
            } else if (actualInvestment < investment) {
                holder.actual.setTextColor(Color.parseColor("#ff0000"))
                holder.dollarSign.setTextColor(Color.parseColor("#ff0000"))
            }


            /**
             *  Picasso for async image loading
             */
            Picasso.with(itemView.context).load(Constants.imageUrl + coin.symbol.toLowerCase() + ".png").into(coinIcon)

            /**
             *  Set color of percentage change textview to reflect
             *  if the percentage change was negative or positive
             */
            oneHourChange.setTextColor(Color.parseColor(when {
                coin.quote.usd.percent_change_1h.contains("-") -> "#ff0000"
                else -> "#32CD32"
            }))

            twentyFourHourChange.setTextColor(Color.parseColor(when {
                coin.quote.usd.percent_change_24h.contains("-") -> "#ff0000"
                else -> "#32CD32"
            }))

            sevenDayChange.setTextColor(Color.parseColor(when {
                coin.quote.usd.percent_change_7d.contains("-") -> "#ff0000"
                else -> "#32CD32"
            }))
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
            builder.setPositiveButton("OK") { dialog, which ->
                run {
                    investment = investmentInput.text.toString().toDouble()
                    cryptoQuantity = quantityInput.text.toString().toFloat()
                    actualInvestment = BigDecimal(cryptoQuantity * coin.quote.usd.price.toDouble() * activity.dollarPrice).setScale(2, RoundingMode.HALF_EVEN).toDouble()
                    holder.initial.text = investment.toString()
                    holder.actual.text = actualInvestment.toString()
                    if (actualInvestment > investment) {
                        holder.actual.setTextColor(Color.parseColor("#32CD32"))
                        holder.dollarSign.setTextColor(Color.parseColor("#32CD32"))
                    } else if (actualInvestment < investment) {
                        holder.actual.setTextColor(Color.parseColor("#ff0000"))
                        holder.dollarSign.setTextColor(Color.parseColor("#ff0000"))
                    }
                    val sharedPref = activity.getSharedPreferences("crypto_tracker", Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putLong("investment_${coin.name}", investment.toRawBits())
                        Log.d("COIN NAME", "quantity_${coin.name}")
                        putFloat("quantity_${coin.name}", cryptoQuantity)
                        Log.d("CRYPTO CUANTITY", cryptoQuantity.toString())
                        apply()
                    }
                }
            }

            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            builder.show()
        }
    }

    class CryptoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /**
         * ViewHolder items (textviews, imageviews) from the crypto_layout.xml
         */
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

    fun updateData(cryptoCoins: Data) {
        val properties = CryptoType.values().map { cryptoType -> Data::class.declaredMemberProperties.firstOrNull { it.name == cryptoType.name.toLowerCase(Locale.ROOT) } }
        this.cryptoCoins = properties.mapNotNull { it?.get(cryptoCoins) as? CryptoModel }
        notifyDataSetChanged()
    }
}


