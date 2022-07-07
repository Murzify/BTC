package ru.mmurzin.btc.api.blockchair

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.mmurzin.btc.api.blockchair.responce.Block
import ru.mmurzin.btc.api.blockchair.responce.Info

interface ApiService {
    @GET("{blockchain}/stats")
    fun getBlockchainStats(@Path("blockchain") blockchain: String): Call<Info>

    @GET("bitcoin/dashboards/block/{block_hash}")
    fun getBlockInfo(@Path("block_hash") blockHash: String): Call<Block>
}
