package ru.murzify.btc.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.murzify.btc.data.api.BlockchainAPI as ApiBlockchainInfo
import ru.murzify.btc.data.api.BlockchairAPI as ApiBlockchair


object Apifactory {
    private val converter: GsonConverterFactory = GsonConverterFactory.create()

    val blockchair: ApiBlockchair = Retrofit.Builder()
        .baseUrl("https://api.blockchair.com/")
        .addConverterFactory(converter)
        .build()
        .create(ApiBlockchair::class.java)

    val blockchainInfo: ApiBlockchainInfo = Retrofit.Builder()
        .baseUrl("https://blockchain.info/")
        .addConverterFactory(converter)
        .build()
        .create(ApiBlockchainInfo::class.java)

    val apiBlockchainInfo: ApiBlockchainInfo = Retrofit.Builder()
        .baseUrl("https://api.blockchain.info/")
        .addConverterFactory(converter)
        .build()
        .create(ApiBlockchainInfo::class.java)
}