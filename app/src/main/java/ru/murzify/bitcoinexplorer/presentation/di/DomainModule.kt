package ru.murzify.bitcoinexplorer.presentation.di

import org.koin.dsl.module
import ru.murzify.bitcoinexplorer.domain.usecase.*

val domainModule = module {

    factory {
        GetGeneralStatsUseCase(repo = get())
    }

    factory {
        GetChartDataUseCase(repo = get())
    }

    factory {
        GetDataAddressUseCase(repo = get())
    }

    factory {
        GetDataBlockUseCase(repo = get())
    }

    factory {
        GetDataTransactionUseCase(repo = get())
    }

}