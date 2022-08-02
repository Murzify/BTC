package ru.murzify.bitcoinexplorer.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.murzify.bitcoinexplorer.domain.models.ApiError
import ru.murzify.bitcoinexplorer.domain.models.BlockData
import ru.murzify.bitcoinexplorer.domain.repository.SearchRepository
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GetDataBlockUseCase(private val repo: SearchRepository) {

    @Suppress("SimpleDateFormat")
    suspend fun execute(hash: String): Either<ApiError, BlockData> {
        lateinit var blockData: Either<ApiError, BlockData>



        repo.searchBlock(hash).fold(
            {
                blockData = it.left()
            },
            {
                if (it.data.isNotEmpty()){
                    it.data[hash]!!.block.also { block ->
                        block.f_input_total = block.input_total / 100000000.0
                        val parseTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(block.time)
                        block.f_time = Timestamp(parseTime!!.time)
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .format(
                                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
                            )
                        block.f_fee_total = block.fee_total / 100000000.0
                        block.f_generation = block.generation / 100000000.0
                    }
                }
                blockData = it.right()
            }
        )
        return blockData
    }

}