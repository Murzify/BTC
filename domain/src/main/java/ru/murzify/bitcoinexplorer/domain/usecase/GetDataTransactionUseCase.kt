package ru.murzify.bitcoinexplorer.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.murzify.bitcoinexplorer.domain.Utils.timeFormat
import ru.murzify.bitcoinexplorer.domain.models.ApiError
import ru.murzify.bitcoinexplorer.domain.models.TransactionData
import ru.murzify.bitcoinexplorer.domain.repository.SearchRepository

class GetDataTransactionUseCase(private val repo: SearchRepository) {

    suspend fun execute(hash: String): Either<ApiError, TransactionData> {
        lateinit var dataTransaction: Either<ApiError, TransactionData>

        repo.searchTransaction(hash).fold(
            {
                dataTransaction = it.left()
            },
            {
                //статус транзакции
                when (it.block_index) {
                    //подтверждена
                    null -> it.status = false
                    //не подтверждена
                    else -> it.status = true
                }

                it.f_time = timeFormat(it.time)

                // общие входы и выходы
                for (input in it.inputs){
                    input.prev_out?.let{ prev_out ->
                        it.f_input += prev_out.value.toDouble()
                    }
                }
                for (out in it.out){
                    it.f_out += out.value.toDouble()
                }
                it.f_out = it.f_out / 100000000
                it.f_input = it.f_input / 100000000

                // комиссия
                it.fee = it.fee / 100000000

                dataTransaction = it.right()
            }
        )

        return dataTransaction
    }

}