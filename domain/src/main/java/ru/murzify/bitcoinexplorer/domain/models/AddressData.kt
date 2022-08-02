package ru.murzify.bitcoinexplorer.domain.models

data class AddressData(
    val address: String,
    val final_balance: Long,
    val n_tx: Int,
    val total_received: Long,
    val total_sent: Long,
    var txs: MutableList<Tx>,

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