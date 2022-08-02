package ru.murzify.bitcoinexplorer.domain.repository

import arrow.core.Either
import ru.murzify.bitcoinexplorer.domain.models.AddressData
import ru.murzify.bitcoinexplorer.domain.models.ApiError
import ru.murzify.bitcoinexplorer.domain.models.BlockData
import ru.murzify.bitcoinexplorer.domain.models.TransactionData

interface SearchRepository {

    suspend fun searchAddress(address: String, offset: Int): Either<ApiError, AddressData>

    suspend fun searchTransaction(hash: String): Either<ApiError, TransactionData>

    suspend fun searchBlock(hash: String): Either<ApiError, BlockData>

}