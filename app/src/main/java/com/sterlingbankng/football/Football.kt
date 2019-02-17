package com.sterlingbankng.football

import android.app.Application
import com.sterlingbankng.football.di.module.appModule
import com.sterlingbankng.football.di.module.networkModule
import com.sterlingbankng.football.di.module.providerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Football: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Football)
            modules(
                listOf(
                    appModule,
                    networkModule,
                    providerModule
                )
            )
        }
    }
}