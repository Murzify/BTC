package ru.murzify.bitcoinexplorer.data.models

import ru.murzify.bitcoinexplorer.domain.models.TransactionData
import ru.murzify.bitcoinexplorer.domain.models.SpendingOutpointX as DomainSpendingOutpointX
import ru.murzify.bitcoinexplorer.domain.models.SpendingOutpoint as DomainSpendingOutpoint
import ru.murzify.bitcoinexplorer.domain.models.PrevOut as DomainPrevOut
import ru.murzify.bitcoinexplorer.domain.models.Out as DomainOut
import ru.murzify.bitcoinexplorer.domain.models.Input as DomainInput

data class Transaction(
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
)

data class Input(
    val index: Int?,
    val prev_out: PrevOut,
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

fun SpendingOutpointX.toDomain() = DomainSpendingOutpointX(
    n,
    tx_index
)

fun SpendingOutpoint.toDomain() = DomainSpendingOutpoint(
    n,
    tx_index
)

fun PrevOut.toDomain() = DomainPrevOut(
    addr,
    n,
    script,
    spending_outpoints.map { it.toDomain() },
    spent,
    tx_index,
    type,
    value
)

fun Out.toDomain() = DomainOut(
    addr,
    n,
    script,
    spending_outpoints.map { it.toDomain() },
    spent,
    tx_index,
    type,
    value
)

fun Input.toDomain() = DomainInput(
    index,
    prev_out.toDomain(),
    script,
    sequence,
    witness
)

fun Transaction.toDomain() = TransactionData(
    block_height,
    block_index,
    double_spend,
    fee,
    hash,
    inputs.map { it.toDomain() },
    lock_time,
    `out`.map { it.toDomain() },
    relayed_by,
    size,
    time,
    tx_index,
    ver,
    vin_sz,
    vout_sz,
    weight
)