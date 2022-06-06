package ru.mmurzin.btc.api.blockchainInfo.responce

data class Transaction(
    val block_height: Int?,
    val block_index: Int?,
    val double_spend: Boolean,
    val fee: Int,
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
    val error: String
)

data class Input(
    val index: Int,
    val prev_out: PrevOut,
    val script: String,
    val sequence: Long,
    val witness: String
)

data class Out(
    val addr: String,
    val n: Int,
    val script: String,
    val spending_outpoints: List<SpendingOutpointX>,
    val spent: Boolean,
    val tx_index: Long,
    val type: Int,
    val value: Long
)

data class PrevOut(
    val addr: String,
    val n: Int,
    val script: String,
    val spending_outpoints: List<SpendingOutpoint>,
    val spent: Boolean,
    val tx_index: Long,
    val type: Int,
    val value: Long
)

data class SpendingOutpoint(
    val n: Int,
    val tx_index: Long
)

data class SpendingOutpointX(
    val n: Int,
    val tx_index: Long
)