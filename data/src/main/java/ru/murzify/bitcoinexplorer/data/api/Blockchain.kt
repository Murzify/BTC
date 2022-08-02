package ru.murzify.bitcoinexplorer.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.murzify.bitcoinexplorer.data.models.Address
import ru.murzify.bitcoinexplorer.data.models.Chart
import ru.murzify.bitcoinexplorer.data.models.Transaction

interface Blockchain {

    @GET("/charts/market-price")
    fun getChart(@QueryMap options: Map<String, String>): Call<Chart>

    @GET("/rawaddr/{address}")
    fun getAddressInfo(
        @Path("address") address: String,
        @QueryMap options: Map<String, String>
    ): Call<Address>

    @GET("/rawtx/{hash}")
    fun getTransactionInfo(@Path("hash") hash: String): Call<Transaction>

}