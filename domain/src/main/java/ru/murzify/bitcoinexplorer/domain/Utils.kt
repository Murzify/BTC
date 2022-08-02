package ru.murzify.bitcoinexplorer.domain

import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Utils {

    fun validSearch(search: String): String {
        val addressRegex = Regex("^(bc1|[13])[a-zA-HJ-NP-Z\\d]{25,39}$")
        val transactionRegex = Regex("^[a-fA-F\\d]{64}\$") // также подходит для хэша блока
        val blockHashRegex = Regex("^0{8}[a-fA-F\\d]{56}\$")
        val blockHeightRegex = Regex("^(0|[1-9]\\d*)\$")

        return if (search.matches(addressRegex)){
            "address"
        } else if (search.matches(blockHashRegex) || search.matches(blockHeightRegex)){
            "block"
        } else if (search.matches(transactionRegex)){
            "transaction"
        } else {
            "invalid"
        }
    }

    fun timeFormat(time: Long): String{
        return Timestamp(time * 1000)
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            )
    }
}