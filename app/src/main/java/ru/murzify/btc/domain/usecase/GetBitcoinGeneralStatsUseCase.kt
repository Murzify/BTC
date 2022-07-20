package ru.murzify.btc.domain.usecase

import ru.murzify.btc.data.api.models.Info
import ru.murzify.btc.domain.repository.InfoRepository

class GetBitcoinGeneralStatsUseCase(private val infoRepository: InfoRepository) {
    suspend fun execute(): Info? {
        val result = infoRepository.getGeneralStats()
        return if (result.isSuccessful){
            result.body()
        } else {
            null
        }
    }
}