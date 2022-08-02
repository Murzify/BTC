package ru.murzify.bitcoinexplorer.domain.models


data class BlockData(
    val `data`: Map<String, DataBlock>
)

data class DataBlock(
    val block: BlockX,
    val transactions: List<String>
)

data class BlockX(
    val date: String,
    val fee_total: Int,
    val generation: Long,
    val guessed_miner: String,
    val hash: String,
    val id: Int,
    val input_count: Int,
    val input_total: Long,
    val nonce: Long,
    val output_count: Int,
    val output_total: Long,
    val reward: Double,
    val size: Long,
    val stripped_size: Int,
    val time: String,
    val transaction_count: Int,
    val weight: Int,
    val witness_count: Int,

    // форматируется в ViewModel
    var f_input_total: Double = 0.0,
    var f_time: String = "",
    var f_fee_total: Double = 0.0,
    var f_generation: Double = 0.0,

)

