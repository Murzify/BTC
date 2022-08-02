package ru.murzify.bitcoinexplorer.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import retrofit2.awaitResponse
import ru.murzify.bitcoinexplorer.data.api.Api
import ru.murzify.bitcoinexplorer.data.models.toDomain
import ru.murzify.bitcoinexplorer.domain.models.ApiError
import ru.murzify.bitcoinexplorer.domain.models.ChartData
import ru.murzify.bitcoinexplorer.domain.models.InfoData
import ru.murzify.bitcoinexplorer.domain.repository.StatsRepository

class StatsRepositoryImpl(private val api: Api): StatsRepository {

    override suspend fun getStats(blockchain: String): Either<ApiError, InfoData> {

        val result = api.blockchair.getBlockchainStats(blockchain).awaitResponse()

        return when (result.isSuccessful) {
            true -> result.body()!!.toDomain().right()
            else -> ApiError(
                code= result.code(),
                message = result.message()
            ).left()
        }
    }

    override suspend fun getChart(): Either<ApiError, ChartData> {

        val result = api.apiBlockchainInfo.getChart(
            mapOf(
                "timespan" to "1months",
                "rollingAverage" to "1days",
                "format" to "json"
            )
        ).awaitResponse()

        return when (result.isSuccessful) {
            true -> result.body()!!.toDomain().right()
            else -> ApiError(
                code= result.code(),
                message = result.message()
            ).left()
        }
    }
}
