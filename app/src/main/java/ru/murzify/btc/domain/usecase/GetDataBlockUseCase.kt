package ru.murzify.btc.domain.usecase

import ru.murzify.btc.data.api.models.Block
import ru.murzify.btc.domain.repository.BlockRepository

class GetDataBlockUseCase(private val blockRepository: BlockRepository) {
    suspend fun execute(blockId: String): Block?{

        val result = blockRepository.getDataBlock(blockId)
        return if (result.isSuccessful){
            result.body()
        } else {
            null
        }
    }
}