package ru.murzify.btc.domain.usecase

import retrofit2.awaitResponse
import ru.murzify.btc.api.Apifactory
import ru.murzify.btc.api.blockchair.responce.Block

class GetDataBlockUseCase {

    lateinit var hash: String

    suspend fun execute(blockId: String): Block?{
        // так пришлось сделать из-за кривого API Blockchair
        hash = blockId
        if (blockId == "0"){
            hash = "000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f"
        }
        val result = Apifactory.blockchair.getBlockInfo(hash).awaitResponse()
        return if (result.isSuccessful){
            result.body()
        } else {
            null
        }
    }
}