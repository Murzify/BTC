package ru.mmurzin.btc.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mmurzin.btc.api.blockchair.ApiService as ApiBlockchair
import ru.mmurzin.btc.api.blockchainInfo.ApiService as ApiBlockchainInfo

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
}