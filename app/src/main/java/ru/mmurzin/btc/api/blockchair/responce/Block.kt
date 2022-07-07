package ru.mmurzin.btc.api.blockchair.responce


data class Block(
    val `data`: Map<String, DataBlock>
)

data class DataBlock(
    val block: BlockX,
    val transactions: List<String>
)

data class BlockX(
    val bits: Int,
    val cdd_total: Double,
    val chainwork: String,
    val coinbase_data_hex: String,
    val date: String,
    val difficulty: Long,
    val fee_per_kb: Double,
    val fee_per_kb_usd: Double,
    val fee_per_kwu: Double,
    val fee_per_kwu_usd: Double,
    val fee_total: Int,
    val fee_total_usd: Double,
    val generation: Int,
    val generation_usd: Int,
    val guessed_miner: String,
    val hash: String,
    val id: Int,
    val input_count: Int,
    val input_total: Long,
    val input_total_usd: Int,
    val median_time: String,
    val merkle_root: String,
    val nonce: Long,
    val output_count: Int,
    val output_total: Long,
    val output_total_usd: Int,
    val reward: Int,
    val reward_usd: Double,
    val size: Int,
    val stripped_size: Int,
    val time: String,
    val transaction_count: Int,
    val version: Int,
    val version_bits: String,
    val version_hex: String,
    val weight: Int,
    val witness_count: Int,

    // форматируется в ViewModel
    var f_input_total: Double,
    var f_time: String,
    var f_fee_total: Double,
    var f_generation: Double,

)
