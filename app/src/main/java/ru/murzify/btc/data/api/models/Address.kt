package ru.murzify.btc.data.api.models

data class Address(
    val address: String,
    val final_balance: Long,
    val hash160: String,
    val n_tx: Int,
    val n_unredeemed: Int,
    val total_received: Long,
    val total_sent: Long,
    var txs: List<Tx>,

    //форматируется в ViewModel
    var f_final_balance: Double = 0.0,
    var f_total_received: Double = 0.0,
    var f_total_sent: Double = 0.0
)

data class Tx(
    val hash: String,
    val time: Long,
    val result: Long
)