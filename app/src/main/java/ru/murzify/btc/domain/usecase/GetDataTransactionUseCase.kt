package ru.murzify.btc.domain.usecase

import retrofit2.awaitResponse
import ru.murzify.btc.api.Apifactory
import ru.murzify.btc.api.blockchainInfo.responce.Transaction

class GetDataTransactionUseCase {
    suspend fun execute(hash: String): Transaction? {
        val result = Apifactory.blockchainInfo.getTransactionInfo(hash).awaitResponse()
        return if (result.isSuccessful){
            result.body()
        } else {
            null
        }
    }
}