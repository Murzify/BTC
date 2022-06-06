package ru.mmurzin.btc.api.blockchair.responce

data class Cache(
    val duration: String,
    val live: Boolean,
    val since: String,
    val time: Double,
    val until: String
)