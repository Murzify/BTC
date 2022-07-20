package ru.murzify.btc.domain.usecase

import ru.murzify.btc.data.api.models.Address
import ru.murzify.btc.domain.repository.AddressRepository

class GetDataAddressUseCase(private val addressRepository: AddressRepository) {
    suspend fun execute(address: String, offset: Int): Address?{
        val result = addressRepository.getAddressData(address, offset)

        return if (result.isSuccessful){
            result.body()
        } else {
            null
        }

    }
}