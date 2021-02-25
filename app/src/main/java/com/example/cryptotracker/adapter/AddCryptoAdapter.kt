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
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.AddCryptoActivity
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


class AddCryptoAdapter(private val activity: AddCryptoActivity) : RecyclerView.Adapter<AddCryptoAdapter.CryptoViewHolder>() {
    private var cryptoCoins: List<CryptoModel> = Collections.emptyList()

    override fun getItemCount(): Int = cryptoCoins.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder = CryptoViewHolder(parent.inflate(R.layout.crypto_list_layout))

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
            builder.setPositiveButton("OK") { dialog, which -> dialog.cancel() }

            builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            builder.show()
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
    }

    fun updateData(cryptoCoins: List<CryptoModel>) {
        this.cryptoCoins = cryptoCoins
        notifyDataSetChanged()
    }

    private fun applyColor(textView: TextView, text: String) {
        textView.setTextColor(activity.resources.getColor(when {
            text.contains("-") -> R.color.red
            else -> R.color.green
        }))
    }
}


