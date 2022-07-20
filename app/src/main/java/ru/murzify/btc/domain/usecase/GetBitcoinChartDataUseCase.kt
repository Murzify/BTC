package ru.murzify.btc.domain.usecase

import ru.murzify.btc.data.api.models.Chart
import ru.murzify.btc.domain.repository.InfoRepository

class GetBitcoinChartDataUseCase(private val blockRepository: InfoRepository) {
    suspend fun execute(): Chart?{
        val result = blockRepository.getChartData()
        return if (result.isSuccessful) {
            result.body()
        } else {
            null
        }
    }
}