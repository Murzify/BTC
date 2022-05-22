package ru.mmurzin.networking.api.blockchainInfo.responce

data class Address(
    val address: String,
    val final_balance: Int,
    val hash160: String,
    val n_tx: Int,
    val n_unredeemed: Int,
    val total_received: Int,
    val total_sent: Int,
    val txs: List<Any>
)