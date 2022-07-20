package ru.murzify.btc.domain.usecase

import ru.murzify.btc.data.api.models.Transaction
import ru.murzify.btc.domain.repository.TransactionRepository

class GetDataTransactionUseCase(private val transactionRepository: TransactionRepository) {
    suspend fun execute(hash: String): Transaction? {
        val result = transactionRepository.getDataTransaction(hash)
        return if (result.isSuccessful){
            result.body()
        } else {
            null
        }
    }
}