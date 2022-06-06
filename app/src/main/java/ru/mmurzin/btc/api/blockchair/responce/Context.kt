package ru.mmurzin.btc.api.blockchair.responce

data class Context(
    val api: Api,
    val cache: Cache,
    val code: Int,
    val full_time: Double,
    val market_price_usd: Int,
    val render_time: Double,
    val request_cost: Int,
    val servers: String,
    val source: String,
    val state: Int,
    val time: Double
)