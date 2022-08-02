package ru.murzify.bitcoinexplorer.data.models

import ru.murzify.bitcoinexplorer.domain.models.AddressData
import ru.murzify.bitcoinexplorer.domain.models.Tx as TxDomain

class Address(
    val address: String,
    val final_balance: Long,
    val n_tx: Int,
    val total_received: Long,
    val total_sent: Long,
    var txs: MutableList<Tx>,
)

data class Tx(
    val hash: String,
    val time: Long,
    val result: Long
)

fun Address.toDomain() = AddressData(
    address = address,
    final_balance = final_balance,
    n_tx = n_tx,
    total_received = total_received,
    total_sent = total_sent,
    txs = txs.map { it.toDomain() } as MutableList<ru.murzify.bitcoinexplorer.domain.models.Tx>
)

fun Tx.toDomain() = TxDomain(
    hash = hash,
    time = time,
    result = result
)
