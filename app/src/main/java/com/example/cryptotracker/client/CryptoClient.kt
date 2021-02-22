package com.example.cryptotracker.client

import com.example.cryptotracker.model.Body
import com.example.cryptotracker.service.CryptoCurrencyService
import com.example.cryptotracker.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/*private val apiUrl = Constants.apiUrl
fun getCoins() {
    /**
     *  enqueue ensures that this call does not occur on the Main UI thread,
     *  network transactions should be done off the Main UI Thread
     */
    var builder: OkHttpClient.Builder? = OkHttpClient().newBuilder()
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BASIC
    builder!!.addInterceptor(interceptor)
    val client = builder.build()
    val retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(CryptoCurrencyService::class.java)

    service.getCryptoCurrency("btc,eth,eos,trx","8f6819ab-b836-43e6-8635-c10ca600265e").enqueue(object : retrofit2.Callback<Body> {
        override fun onResponse(
            call: retrofit2.Call<Body>,
            response: retrofit2.Response<Body>
        ) {
            println("body: " + response.body())
            runOnUiThread {
                response.body()?.let { adapter.updateData(it.data) }
            }
        }

        override fun onFailure(call: retrofit2.Call<Body>, t: Throwable) {
            println("Failed $t")
        }

    })
}*/