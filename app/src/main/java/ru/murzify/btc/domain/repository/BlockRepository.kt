package ru.murzify.btc.domain.repository

import retrofit2.Response
import ru.murzify.btc.data.api.models.Block

interface BlockRepository {
    suspend fun getDataBlock(blockId: String): Response<Block>
}