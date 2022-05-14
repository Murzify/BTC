package ru.mmurzin.networking.api.blockchair.responce

data class Api(
    val documentation: String,
    val last_major_update: String,
    val next_major_update: Any,
    val notice: String,
    val version: String
)