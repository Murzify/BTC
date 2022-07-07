package ru.mmurzin.btc.api.blockchair.responce

data class Info(
    val context: Context,
    val `data`: Data
)

data class Context(
    val api: Api,
    val cache: Cache,
    val code: Int,
    val full_time: Double,
    val market_price_usd: Double,
    val render_time: Double,
    val request_cost: Int,
    val servers: String,
    val source: String,
    val state: Int,
    val time: Double
)

data class Data(
    val average_transaction_fee_24h: Int,
    val average_transaction_fee_usd_24h: Double,
    val best_block_hash: String,
    val best_block_height: Int,
    val best_block_time: String,
    val blockchain_size: Long,
    val blocks: Int,
    val blocks_24h: Int,
    val cdd_24h: Double,
    val circulation: Long,
    val countdowns: List<Any>,
    val difficulty: Long,
    val hashrate_24h: String,
    val hodling_addresses: Int,
    val inflation_24h: Long,
    val inflation_usd_24h: Double,
    val largest_transaction_24h: LargestTransaction24h,
    val market_cap_usd: Long,
    val market_dominance_percentage: Double,
    val market_price_btc: Double,
    val market_price_usd: Double,
    val market_price_usd_change_24h_percentage: Double,
    val median_transaction_fee_24h: Int,
    val median_transaction_fee_usd_24h: Double,
    val mempool_outputs: Int,
    val mempool_size: Int,
    val mempool_total_fee_usd: Double,
    val mempool_tps: Float,
    val mempool_transactions: Int,
    val next_difficulty_estimate: Long,
    val next_retarget_time_estimate: String,
    val nodes: Int,
    val outputs: Int,
    val suggested_transaction_fee_per_byte_sat: Int,
    val transactions: Int,
    val transactions_24h: Int,
    val volume_24h: Long
)

data class Api(
    val documentation: String,
    val last_major_update: String,
    val next_major_update: Any,
    val notice: String,
    val version: String
)

data class Cache(
    val duration: String,
    val live: Boolean,
    val since: String,
    val time: Double,
    val until: String
)

data class LargestTransaction24h(
    val hash: String,
    val value_usd: Int
)