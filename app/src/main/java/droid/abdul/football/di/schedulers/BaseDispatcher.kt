package droid.abdul.football.di.schedulers

import kotlinx.coroutines.CoroutineDispatcher

interface BaseDispatcher {
    fun computation(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun ui(): CoroutineDispatcher
}