package droid.abdul.football.di.module

import droid.abdul.football.di.schedulers.BaseScheduler
import droid.abdul.football.di.schedulers.SchedulerProvider
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