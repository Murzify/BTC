package ru.murzify.btc.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.murzify.btc.data.api.models.Block
import ru.murzify.btc.data.api.models.Info

interface BlockchairAPI {
    @GET("{blockchain}/stats")
    fun getBlockchainStats(@Path("blockchain") blockchain: String): Call<Info>

    @GET("bitcoin/dashboards/block/{block_hash}")
    fun getBlockInfo(@Path("block_hash") blockHash: String): Call<Block>
}
