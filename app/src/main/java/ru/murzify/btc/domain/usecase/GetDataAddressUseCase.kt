package ru.murzify.btc.domain.usecase

import retrofit2.awaitResponse
import ru.murzify.btc.api.Apifactory
import ru.murzify.btc.api.blockchainInfo.responce.Address

class GetDataAddressUseCase {
    suspend fun execute(address: String, offset: Int): Address?{
        val result = Apifactory.blockchainInfo.getAddressInfo(address, mapOf(
            "offset" to offset.toString()
        )).awaitResponse()

        return if (result.isSuccessful){
            result.body()
        } else {
            null
        }

    }
}