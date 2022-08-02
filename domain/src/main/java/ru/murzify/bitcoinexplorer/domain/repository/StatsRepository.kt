package ru.murzify.bitcoinexplorer.domain.repository

import arrow.core.Either
import ru.murzify.bitcoinexplorer.domain.models.ApiError
import ru.murzify.bitcoinexplorer.domain.models.ChartData
import ru.murzify.bitcoinexplorer.domain.models.InfoData

interface StatsRepository {

    suspend fun getStats(blockchain: String): Either<ApiError, InfoData>

    suspend fun getChart(): Either<ApiError, ChartData>

}