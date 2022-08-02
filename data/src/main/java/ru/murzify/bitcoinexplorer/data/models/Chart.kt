package ru.murzify.bitcoinexplorer.data.models

import ru.murzify.bitcoinexplorer.domain.models.ChartData
import ru.murzify.bitcoinexplorer.domain.models.Value as DomainValue

data class Chart(
    val description: String,
    val name: String,
    val period: String,
    val status: String,
    val unit: String,
    val values: List<Value>,

    var isUp: Boolean = false,
    var percent: Double = 1.0
)
data class Value(
    val x: Long,
    val y: Double
)

fun Value.toDomain() = DomainValue(
    x,
    y
)

fun Chart.toDomain() = ChartData(
    values.map { it.toDomain() }
)