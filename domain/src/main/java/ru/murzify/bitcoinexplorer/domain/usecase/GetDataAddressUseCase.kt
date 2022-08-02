package ru.murzify.bitcoinexplorer.domain.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.murzify.bitcoinexplorer.domain.models.AddressData
import ru.murzify.bitcoinexplorer.domain.models.ApiError
import ru.murzify.bitcoinexplorer.domain.repository.SearchRepository

class GetDataAddressUseCase(private val repo: SearchRepository) {

    suspend fun execute(address: String, offset: Int): Either<ApiError, AddressData> {
        lateinit var addressData: Either<ApiError, AddressData>

        repo.searchAddress(address, offset).fold(
                {
                    addressData = it.left()
                },
                {
                    // выведутся перывые 100 транзакций
                    // если offset > 0 то нужно вывести последующие транзакции,
                    // сохранять во ViewModel их не нужно
                    if (offset == 0) {
                        it.f_final_balance = it.final_balance / 100000000.0
                        it.f_total_received = it.total_received / 100000000.0
                        it.f_total_sent = it.total_sent / 100000000.0
                    }
                    addressData = it.right()
                }
        )

        return addressData
    }
}