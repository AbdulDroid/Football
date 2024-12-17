package droid.abdul.football.di.module

import droid.abdul.football.di.schedulers.BaseDispatcher
import droid.abdul.football.di.schedulers.DispatcherProvider
import org.koin.dsl.module

val providerModule = module {
    single { createDispatcher() }
    single { dispatcher() }
}

fun createDispatcher(): BaseDispatcher {
    return DispatcherProvider.instance
}

fun dispatcher(): DispatcherProvider {
    return DispatcherProvider.instance
}