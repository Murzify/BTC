package ru.murzify.bitcoinexplorer.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.murzify.bitcoinexplorer.domain.models.ApiError
import ru.murzify.bitcoinexplorer.domain.models.ChartData
import ru.murzify.bitcoinexplorer.domain.repository.StatsRepository
import kotlin.math.abs

class GetChartDataUseCase(private val repo: StatsRepository) {

    suspend fun execute(): Either<ApiError, ChartData> {
        lateinit var chartData: Either<ApiError, ChartData>

        repo.getChart().fold(
            {
                chartData = it.left()
            },
            { chart ->
                chart.values.also { values ->
                    val start = values[0].y
                    val end = values[values.size - 1].y
                    var isUp = false
                    if (start < end){
                        isUp = true
                    }
                    val percent = abs((start-end)/((start+end)/2)) * 100
                    chart.isUp = isUp
                    chart.percent = percent
                }
                chartData = chart.right()
            }
        )

        return chartData

    }

}