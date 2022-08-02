package ru.murzify.bitcoinexplorer.data.models

import ru.murzify.bitcoinexplorer.domain.models.BlockX as DomainBlockX
import ru.murzify.bitcoinexplorer.domain.models.DataBlock as DomainDataBlock
import ru.murzify.bitcoinexplorer.domain.models.BlockData

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
    val difficulty: Double,
    val fee_per_kb: Double,
    val fee_per_kb_usd: Double,
    val fee_per_kwu: Double,
    val fee_per_kwu_usd: Double,
    val fee_total: Int,
    val fee_total_usd: Double,
    val generation: Long,
    val generation_usd: Double,
    val guessed_miner: String,
    val hash: String,
    val id: Int,
    val input_count: Int,
    val input_total: Long,
    val input_total_usd: Double,
    val median_time: String,
    val merkle_root: String,
    val nonce: Long,
    val output_count: Int,
    val output_total: Long,
    val output_total_usd: Double,
    val reward: Double,
    val reward_usd: Double,
    val size: Long,
    val stripped_size: Int,
    val time: String,
    val transaction_count: Int,
    val version: Int,
    val version_bits: String,
    val version_hex: String,
    val weight: Int,
    val witness_count: Int,
)


fun BlockX.toDomain() = DomainBlockX(
    date,
    fee_total,
    generation,
    guessed_miner,
    hash,
    id,
    input_count,
    input_total,
    nonce,
    output_count,
    output_total,
    reward,
    size,
    stripped_size,
    time,
    transaction_count,
    weight,
    witness_count
)

fun DataBlock.toDomain() = DomainDataBlock(
    block.toDomain(),
    transactions
)

fun Block.toDomain() = BlockData(
    `data`.map { it.key to it.value.toDomain() }.toMap()
)
