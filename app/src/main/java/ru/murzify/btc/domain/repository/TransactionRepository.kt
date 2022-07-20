package ru.murzify.btc.domain.repository

import retrofit2.Response
import ru.murzify.btc.data.api.models.Transaction

interface TransactionRepository {
    suspend fun getDataTransaction(hash: String): Response<Transaction>
}