package ru.mmurzin.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Apifactory {

    val blockchair: ApiService = Retrofit.Builder()
        .baseUrl("https://api.blockchair.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}