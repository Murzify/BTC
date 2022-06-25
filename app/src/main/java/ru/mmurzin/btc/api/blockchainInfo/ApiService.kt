package ru.mmurzin.btc.api.blockchainInfo

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.mmurzin.btc.api.blockchainInfo.responce.Address
import ru.mmurzin.btc.api.blockchainInfo.responce.Chart
import ru.mmurzin.btc.api.blockchainInfo.responce.Transaction

interface ApiService {
    @GET("/rawaddr/{address}")
    fun getAddressInfo(@Path("address") address: String): Call<Address>

    @GET("/rawtx/{hash}")
    fun getTransactionInfo(@Path("hash") hash: String): Call<Transaction>

    @GET("/charts/market-price")
    fun getChart(@QueryMap options: Map<String, String>): Call<Chart>
}