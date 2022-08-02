package ru.murzify.bitcoinexplorer.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.murzify.bitcoinexplorer.presentation.SearchViewModel
import ru.murzify.bitcoinexplorer.presentation.StatsViewModel
import ru.murzify.bitcoinexplorer.presentation.fragments.*


val appModule = module {

    single {
        StatsFragment()
    }

    single {
        SearchFragment()
    }

    single {
        AddressFragment()
    }

    single {
        BlockFragment()
    }

    single {
        TransactionFragment()
    }

    viewModel {
        StatsViewModel(
            getGeneralStatsUseCase = get(),
            getChartDataUseCase = get()
        )
    }

    viewModel {
        SearchViewModel(
            getDataAddressUseCase = get(),
            getDataBlockUseCase = get(),
            getDataTransactionUseCase = get(),
        )
    }
}