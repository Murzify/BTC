package ru.murzify.bitcoinexplorer.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Api {

    private val converter: GsonConverterFactory = GsonConverterFactory.create()

    val blockchair: Blockchair = Retrofit.Builder()
        .baseUrl("https://api.blockchair.com/")
        .addConverterFactory(converter)
        .build()
        .create(Blockchair::class.java)

    val apiBlockchainInfo: Blockchain = Retrofit.Builder()
        .baseUrl("https://api.blockchain.info/")
        .addConverterFactory(converter)
        .build()
        .create(Blockchain::class.java)

    val blockchainInfo: Blockchain = Retrofit.Builder()
        .baseUrl("https://blockchain.info/")
        .addConverterFactory(converter)
        .build()
        .create(Blockchain::class.java)
}