package ru.mmurzin.btc.api.blockchainInfo.responce

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