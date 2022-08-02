package ru.murzify.bitcoinexplorer.domain.models

data class ChartData(
    val values: List<Value>,

    var isUp: Boolean = false,
    var percent: Double = 1.0
)
data class Value(
    val x: Long,
    val y: Double
)