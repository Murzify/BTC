package ru.murzify.btc.domain.repository

import retrofit2.Response
import ru.murzify.btc.data.api.models.Address

interface AddressRepository {
    suspend fun getAddressData(address: String, offset: Int): Response<Address>
}