package com.example.cryptotracker.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.preference.PreferenceManager
import android.provider.Settings.Global.getString
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.MainActivity
import com.example.cryptotracker.R
import com.example.cryptotracker.model.CryptoModel
import com.example.cryptotracker.model.Item
import com.example.cryptotracker.service.DolarService
import com.example.cryptotracker.utils.Constants
import com.example.cryptotracker.utils.Constants.dolarApiUrl
import com.example.cryptotracker.utils.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.crypto_layout.view.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.util.*


/**
 * Created by CharlesAE on 2/25/18.
 */
class CryptoAdapter(activity: MainActivity) : RecyclerView.Adapter<CryptoAdapter.CryptoViewHolder>() {
    private var cryptoCoins: List<CryptoModel> = Collections.emptyList()
    private var dollarPrice: Double = 0.0
    private var cryptoQuantity: Double = 0.0
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
        getDollarPrice()
        val dolar = dollarPrice
        /**
         * get CryptoModel data and bind them to corresponding
         * viewholder widgets(text view, imageview etc)
         */
        holder.apply {
            coinName.text = coin.name
            coinSymbol.text = coin.symbol
            coinPrice.text = BigDecimal(coin.quote.usd.price.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
            oneHourChange.text = BigDecimal(coin.quote.usd.percent_change_1h.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()  + "%"
            twentyFourHourChange.text = BigDecimal(coin.quote.usd.percent_change_24h.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()  + "%"
            sevenDayChange.text = BigDecimal(coin.quote.usd.percent_change_7d.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()  + "%"

            val sharedPref =  activity.getPreferences(Context.MODE_PRIVATE) ?: return
            investment = Double.fromBits(sharedPref.getLong("investment_${coin.name}", 0))
            cryptoQuantity =  Double.fromBits(sharedPref.getLong("quantity_${coin.name}", 0))
            actualInvestment = BigDecimal(cryptoQuantity * coin.quote.usd.price.toDouble() * dollarPrice).setScale(2, RoundingMode.HALF_EVEN).toDouble()
            holder.actual.text = actualInvestment.toString()
            holder.initial.text = investment.toString()
            val color = if (actualInvestment > investment)  "#32CD32" else "#ff0000"
            holder.actual.setTextColor(Color.parseColor(color))
            holder.dollarSign.setTextColor(Color.parseColor(color))

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
                    cryptoQuantity = quantityInput.text.toString().toDouble()
                    actualInvestment = BigDecimal(cryptoQuantity * coin.quote.usd.price.toDouble() * dollarPrice).setScale(2, RoundingMode.HALF_EVEN).toDouble()
                    holder.initial.text = investment.toString()
                    holder.actual.text = actualInvestment.toString()
                    val color = if (actualInvestment > investment)  "#32CD32" else "#ff0000"
                    holder.actual.setTextColor(Color.parseColor(color))
                    holder.dollarSign.setTextColor(Color.parseColor(color))
                    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
                    with (sharedPref.edit()) {
                        putLong("investment_${coin.name}", investment.toRawBits())
                        putLong("quantity_${coin.name}", cryptoQuantity.toRawBits())
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

    fun updateData(cryptoCoins: List<CryptoModel>) {
        this.cryptoCoins = cryptoCoins
        notifyDataSetChanged()
    }

    fun getDollarPrice() {
        val builder: OkHttpClient.Builder? = OkHttpClient().newBuilder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        builder!!.addInterceptor(interceptor)
        val client = builder.build()
        val retrofit = Retrofit.Builder()
                .baseUrl(dolarApiUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val service = retrofit.create(DolarService::class.java)

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
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Item>>, t: Throwable) {
                println("Failed $t")
            }

        })
    }
}


