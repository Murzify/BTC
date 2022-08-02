package ru.murzify.bitcoinexplorer.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ru.murzify.bitcoinexplorer.presentation.di.appModule
import ru.murzify.bitcoinexplorer.presentation.di.dataModule
import ru.murzify.bitcoinexplorer.presentation.di.domainModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                appModule,
                dataModule,
                domainModule
            )
        }
    }
}