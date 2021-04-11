package com.example.cryptotracker.presentation.add

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.presentation.main.MainActivity
import com.example.cryptotracker.R
import com.example.cryptotracker.presentation.Constants
import com.example.cryptotracker.presentation.getTwoDigitsDouble
import com.example.cryptotracker.presentation.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.crypto_layout.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


internal class Adapter(
        private val newCryptoListener: NewCryptoListener
) : RecyclerView.Adapter<Adapter.CryptoViewHolder>(), Filterable {

    private var cryptoCoins: List<ViewModel.CryptoCurrencyViewData> = Collections.emptyList()
    private var filteredCryptoCoins: List<ViewModel.CryptoCurrencyViewData> = Collections.emptyList()

    init {
        filteredCryptoCoins = cryptoCoins
    }

    fun updateData(cryptoCoins: List<ViewModel.CryptoCurrencyViewData>) {
        this.cryptoCoins = cryptoCoins
        this.filteredCryptoCoins = cryptoCoins
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = filteredCryptoCoins.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder = CryptoViewHolder(parent.inflate(R.layout.crypto_list_layout))

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredCryptoCoins = if (charSearch.isEmpty()) {
                    cryptoCoins
                } else {
                    cryptoCoins.filter { it.name.toLowerCase(Locale.US).contains(charSearch.toLowerCase(Locale.US)) }
                }
                return FilterResults().apply {
                    values = filteredCryptoCoins
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCryptoCoins = results?.values as ArrayList<ViewModel.CryptoCurrencyViewData>
                notifyDataSetChanged()
            }

        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val currency = filteredCryptoCoins[position]

        holder.bindData(currency) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(it.context)
            builder.setTitle("Cálculo de Inversion")

            val lila1 = LinearLayout(it.context)
            lila1.orientation = LinearLayout.VERTICAL
            val investmentTextView = TextView(it.context)
            val quantityTextView = TextView(it.context)
            investmentTextView.text = it.resources.getString(R.string.investmentTV)
            quantityTextView.text = it.resources.getString(R.string.quantityTV)
            val investmentInput = EditText(it.context)
            val quantityInput = EditText(it.context)
            investmentInput.hint = "Ingrese monto invertido"
            quantityInput.hint = "Ingrese cantidad de cripto"
            investmentInput.inputType = InputType.TYPE_CLASS_NUMBER.or(InputType.TYPE_NUMBER_FLAG_DECIMAL)
            quantityInput.inputType = InputType.TYPE_CLASS_NUMBER.or(InputType.TYPE_NUMBER_FLAG_DECIMAL)
            lila1.addView(investmentTextView)
            lila1.addView(investmentInput)
            lila1.addView(quantityTextView)
            lila1.addView(quantityInput)
            builder.setView(lila1)
            builder.setPositiveButton("OK") { _, _ ->
                newCryptoListener.onNewCryptoAdded(currency, NewCryptoListener.InvestmentData(
                        investmentInput.text.toString().toDouble(),
                        quantityInput.text.toString().toFloat()
                ))
            }

            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            builder.show()
        }

    }

    class CryptoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val coinName: TextView = view.coinName
        private val coinSymbol: TextView = view.coinSymbol
        private val coinPrice: TextView = view.priceUsdText
        private val oneHourChange: TextView = view.percentChange1hText
        private val twentyFourHourChange: TextView = view.percentChange24hText
        private val sevenDayChange: TextView = view.percentChange7dayText
        private val coinIcon: ImageView = view.coinIcon

        fun bindData(currency: ViewModel.CryptoCurrencyViewData, onClickListener: (View) -> Unit) {
            coinName.text = currency.name
            coinSymbol.text = currency.symbol
            coinPrice.text = getTwoDigitsDouble(currency.quote.usd.price)
            oneHourChange.text = "${getTwoDigitsDouble(currency.quote.usd.percent_change_1h)}%"
            twentyFourHourChange.text = "${getTwoDigitsDouble(currency.quote.usd.percent_change_24h)}%"
            sevenDayChange.text = "${getTwoDigitsDouble(currency.quote.usd.percent_change_7d)}%"

            Picasso.with(itemView.context).load(Constants.imageUrl + currency.symbol.toLowerCase(Locale.US) + ".png").into(coinIcon)

            applyColor(oneHourChange, currency.quote.usd.percent_change_1h)

            applyColor(twentyFourHourChange, currency.quote.usd.percent_change_24h)

            applyColor(sevenDayChange, currency.quote.usd.percent_change_7d)

            itemView.setOnClickListener { onClickListener(itemView) }
        }

        private fun applyColor(textView: TextView, text: String) {
            textView.setTextColor(ContextCompat.getColor(itemView.context, when {
                text.contains("-") -> R.color.red
                else -> R.color.green
            }))
        }
    }
}


