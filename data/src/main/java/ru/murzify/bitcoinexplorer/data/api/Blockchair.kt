package ru.murzify.bitcoinexplorer.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.murzify.bitcoinexplorer.data.models.Block
import ru.murzify.bitcoinexplorer.data.models.Info

interface Blockchair {

    @GET("{blockchain}/stats")
    fun getBlockchainStats(@Path("blockchain") blockchain: String): Call<Info>

    @GET("bitcoin/dashboards/block/{block_hash}")
    fun getBlockInfo(@Path("block_hash") blockHash: String): Call<Block>

}