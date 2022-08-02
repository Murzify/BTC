package ru.murzify.bitcoinexplorer.domain.models

data class TransactionData(
    val block_height: Int?,
    val block_index: Int?,
    val double_spend: Boolean,
    var fee: Double,
    val hash: String,
    val inputs: List<Input>,
    val lock_time: Int,
    val `out`: List<Out>,
    val relayed_by: String,
    val size: Int,
    val time: Long,
    val tx_index: Long,
    val ver: Int,
    val vin_sz: Int,
    val vout_sz: Int,
    val weight: Int,

    //форматируется
    var f_time: String = "",
    var status: Boolean = false,
    var f_input: Double = 0.0,
    var f_out: Double = 0.0
)

data class Input(
    val index: Int?,
    val prev_out: PrevOut?,
    val script: String?,
    val sequence: Long?,
    val witness: String?
)

data class Out(
    val addr: String?,
    val n: Int?,
    val script: String?,
    val spending_outpoints: List<SpendingOutpointX>,
    val spent: Boolean?,
    val tx_index: Long?,
    val type: Int?,
    val value: Long
)

data class PrevOut(
    val addr: String?,
    val n: Long?,
    val script: String?,
    val spending_outpoints: List<SpendingOutpoint>,
    val spent: Boolean?,
    val tx_index: Long?,
    val type: Int?,
    val value: Long
)

data class SpendingOutpoint(
    val n: Long,
    val tx_index: Long
)

data class SpendingOutpointX(
    val n: Long,
    val tx_index: Long
)

