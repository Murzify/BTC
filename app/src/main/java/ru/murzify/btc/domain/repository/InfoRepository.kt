package ru.murzify.btc.domain.repository

import retrofit2.Response
import ru.murzify.btc.data.api.models.Chart
import ru.murzify.btc.data.api.models.Info

interface InfoRepository {

    suspend fun getGeneralStats(): Response<Info>

    suspend fun getChartData(): Response<Chart>
}