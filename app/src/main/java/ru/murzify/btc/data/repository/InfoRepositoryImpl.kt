package ru.murzify.btc.data.repository

import retrofit2.Response
import retrofit2.awaitResponse
import ru.murzify.btc.data.api.Apifactory
import ru.murzify.btc.data.api.models.Chart
import ru.murzify.btc.data.api.models.Info
import ru.murzify.btc.domain.repository.InfoRepository

class InfoRepositoryImpl(private val api: Apifactory): InfoRepository {
    override suspend fun getGeneralStats(): Response<Info> {
        return api.blockchair.getBlockchainStats("bitcoin").awaitResponse()
    }

    override suspend fun getChartData(): Response<Chart> {
        return api.apiBlockchainInfo.getChart(
            mapOf(
                "timespan" to "1months",
                "rollingAverage" to "1days",
                "format" to "json"
            )
        ).awaitResponse()
    }

}