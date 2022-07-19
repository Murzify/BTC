package ru.murzify.btc.domain.usecase

import retrofit2.awaitResponse
import ru.murzify.btc.api.Apifactory
import ru.murzify.btc.api.blockchainInfo.responce.Chart

class GetBitcoinChartDataUseCase {
    suspend fun execute(): Chart?{
        val result = Apifactory.apiBlockchainInfo.getChart(
            mapOf(
                "timespan" to "1months",
                "rollingAverage" to "1days",
                "format" to "json"
            )
        ).awaitResponse()
        return if (result.isSuccessful) {
            result.body()
        } else {
            null
        }
    }
}