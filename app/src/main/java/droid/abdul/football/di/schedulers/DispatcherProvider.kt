package droid.abdul.football.di.schedulers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DispatcherProvider: BaseDispatcher {
    override fun computation(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun ui(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    companion object {

        private var INSTANCE: DispatcherProvider? = null

        @Synchronized
        private fun createInstance() {
            if (INSTANCE == null) {
                INSTANCE =
                        DispatcherProvider()
            }
        }

        val instance: DispatcherProvider
            get() {
                if (INSTANCE == null) createInstance()
                return INSTANCE!!
            }
    }
}