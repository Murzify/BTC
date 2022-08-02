package ru.murzify.bitcoinexplorer.domain.models

data class ApiError(
    val code: Int,
    val message: String
)