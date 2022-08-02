package ru.murzify.bitcoinexplorer.presentation.di

import org.koin.dsl.module
import ru.murzify.bitcoinexplorer.data.api.Api
import ru.murzify.bitcoinexplorer.data.repository.SearchRepositoryImpl
import ru.murzify.bitcoinexplorer.data.repository.StatsRepositoryImpl
import ru.murzify.bitcoinexplorer.domain.repository.SearchRepository
import ru.murzify.bitcoinexplorer.domain.repository.StatsRepository

val dataModule = module {

    single {
        Api()
    }

    single<StatsRepository> {
        StatsRepositoryImpl(api = get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(api = get())
    }

}