package ru.mmurzin.networking.api.blockchainInfo.responce

data class Transaction(
    val block_height: Int,
    val hash: String,
    val inputs: List<Input>,
    val lock_time: String,
    val `out`: List<Out>,
    val relayed_by: String,
    val size: Int,
    val tx_index: String,
    val ver: Int,
    val vin_sz: Int,
    val vout_sz: Int
)

data class PrevOut(
    val hash: String,
    val n: String,
    val tx_index: String,
    val value: String
)

data class Out(
    val hash: String,
    val script: String,
    val value: String
)

data class Input(
    val prev_out: PrevOut,
    val script: String
)
