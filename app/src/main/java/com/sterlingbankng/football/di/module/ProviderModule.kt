package com.sterlingbankng.football.di.module

import com.sterlingbankng.football.di.schedulers.BaseScheduler
import com.sterlingbankng.football.di.schedulers.SchedulerProvider
import org.koin.dsl.module

val providerModule = module {
    single { createScheduler() }
    single { scheduler() }
}

fun createScheduler(): BaseScheduler {
    return SchedulerProvider.instance
}

fun scheduler(): SchedulerProvider {
    return SchedulerProvider.instance
}