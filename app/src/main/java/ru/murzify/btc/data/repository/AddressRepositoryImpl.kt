package ru.murzify.btc.data.repository

import retrofit2.Response
import retrofit2.awaitResponse
import ru.murzify.btc.data.api.Apifactory
import ru.murzify.btc.data.api.models.Address
import ru.murzify.btc.domain.repository.AddressRepository

class AddressRepositoryImpl(private val api: Apifactory): AddressRepository {
    override suspend fun getAddressData(address: String, offset: Int): Response<Address> {
        return api.blockchainInfo.getAddressInfo(address, mapOf(
            "offset" to offset.toString()
        )).awaitResponse()
    }
}