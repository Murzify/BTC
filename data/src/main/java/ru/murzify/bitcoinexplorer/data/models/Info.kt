package ru.murzify.bitcoinexplorer.data.models

import ru.murzify.bitcoinexplorer.domain.models.InfoData
import ru.murzify.bitcoinexplorer.domain.models.LargestTransaction24h as DomainLatestTransaction24h
import ru.murzify.bitcoinexplorer.domain.models.Data as DomainData

data class Info(
    val `data`: Data
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

data class LargestTransaction24h(
    val hash: String,
    val value_usd: Int
)

fun LargestTransaction24h.toDomain() = DomainLatestTransaction24h(
    hash,
    value_usd
)

fun Data.toDomain() = DomainData(
    average_transaction_fee_24h,
    average_transaction_fee_usd_24h,
    best_block_hash,
    best_block_height,
    best_block_time,
    blockchain_size,
    blocks,
    blocks_24h,
    cdd_24h,
    circulation,
    countdowns,
    difficulty,
    hashrate_24h,
    hodling_addresses,
    inflation_24h,
    inflation_usd_24h,
    largest_transaction_24h.toDomain(),
    market_cap_usd,
    market_dominance_percentage,
    market_price_btc,
    market_price_usd,
    market_price_usd_change_24h_percentage,
    median_transaction_fee_24h,
    median_transaction_fee_usd_24h,
    mempool_outputs,
    mempool_size,
    mempool_total_fee_usd,
    mempool_tps,
    mempool_transactions,
    next_difficulty_estimate,
    next_retarget_time_estimate,
    nodes,
    outputs,
    suggested_transaction_fee_per_byte_sat,
    transactions,
    transactions_24h,
    volume_24h
)

fun Info.toDomain() = InfoData(
    data.toDomain()
)