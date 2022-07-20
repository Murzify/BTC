package ru.murzify.btc.data.repository

import retrofit2.Response
import retrofit2.awaitResponse
import ru.murzify.btc.data.api.Apifactory
import ru.murzify.btc.data.api.models.Transaction
import ru.murzify.btc.domain.repository.TransactionRepository

class TransactionRepositoryImpl(private val api: Apifactory): TransactionRepository {
    override suspend fun getDataTransaction(hash: String): Response<Transaction> {
        return api.blockchainInfo.getTransactionInfo(hash).awaitResponse()
    }
}