package ru.murzify.bitcoinexplorer.data.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import retrofit2.Response
import retrofit2.awaitResponse
import ru.murzify.bitcoinexplorer.data.api.Api
import ru.murzify.bitcoinexplorer.data.models.toDomain
import ru.murzify.bitcoinexplorer.domain.models.AddressData
import ru.murzify.bitcoinexplorer.domain.models.ApiError
import ru.murzify.bitcoinexplorer.domain.models.BlockData
import ru.murzify.bitcoinexplorer.domain.models.TransactionData
import ru.murzify.bitcoinexplorer.domain.repository.SearchRepository

class SearchRepositoryImpl(private val api: Api): SearchRepository {

    override suspend fun searchAddress(address: String, offset: Int): Either<ApiError, AddressData> {
        val result = api.blockchainInfo.getAddressInfo(address, options=mapOf(
            "offset" to offset.toString()
        )).awaitResponse()

        return when (result.isSuccessful) {
            true -> result.body()!!.toDomain().right()
            else -> ApiError(
                code= result.code(),
                message = result.message()
            ).left()
        }
    }

    override suspend fun searchTransaction(hash: String): Either<ApiError, TransactionData> {
        val result = api.blockchainInfo.getTransactionInfo(hash).awaitResponse()

        return when (result.isSuccessful) {
            true -> result.body()!!.toDomain().right()
            else -> ApiError(
                code= result.code(),
                message = result.message()
            ).left()
        }
    }

    override suspend fun searchBlock(hash: String): Either<ApiError, BlockData> {
        val result = api.blockchair.getBlockInfo(hash).awaitResponse()

        return when (result.isSuccessful) {
            true -> result.body()!!.toDomain().right()
            else -> ApiError(
                code= result.code(),
                message = result.message()
            ).left()
        }
    }

}
