package ru.mmurzin.networking.api.blockchainInfo

import retrofit2.http.GET
import retrofit2.http.Path
import ru.mmurzin.networking.api.blockchainInfo.responce.Address
import ru.mmurzin.networking.api.blockchainInfo.responce.Transaction

interface ApiService {
    @GET("/rawaddr/{address}")
    fun getAddressInfo(@Path("address") address: String): Address

    @GET("/rawtx/{hash}")
    fun getTransactionInfo(@Path("hash") hash: String): Transaction
}