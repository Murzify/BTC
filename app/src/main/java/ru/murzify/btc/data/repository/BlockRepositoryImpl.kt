package ru.murzify.btc.data.repository

import retrofit2.Response
import retrofit2.awaitResponse
import ru.murzify.btc.data.api.Apifactory
import ru.murzify.btc.data.api.models.Block
import ru.murzify.btc.domain.repository.BlockRepository

class BlockRepositoryImpl(private val api: Apifactory): BlockRepository {
    lateinit var hash: String

    override suspend fun getDataBlock(blockId: String): Response<Block> {
        // так пришлось сделать из-за кривого API Blockchair
        hash = blockId
        if (blockId == "0"){
            hash = "000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f"
        }
        return api.blockchair.getBlockInfo(hash).awaitResponse()
    }
}