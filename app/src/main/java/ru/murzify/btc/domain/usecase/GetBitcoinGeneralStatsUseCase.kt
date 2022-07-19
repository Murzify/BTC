package ru.murzify.btc.domain.usecase

import retrofit2.awaitResponse
import ru.murzify.btc.api.Apifactory
import ru.murzify.btc.api.blockchair.responce.Info

class GetBitcoinGeneralStatsUseCase {
    suspend fun execute(): Info? {
        val result = Apifactory.blockchair.getBlockchainStats("bitcoin").awaitResponse()
        return if (result.isSuccessful){
            result.body()
        } else {
            null
        }
    }
}