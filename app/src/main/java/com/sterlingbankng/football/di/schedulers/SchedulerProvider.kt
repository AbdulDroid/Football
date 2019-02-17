package com.sterlingbankng.football.di.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulerProvider: BaseScheduler {
    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    companion object {

        private var INSTANCE: SchedulerProvider? = null

        @Synchronized
        private fun createInstance() {
            if (INSTANCE == null) {
                INSTANCE =
                        SchedulerProvider()
            }
        }

        val instance: SchedulerProvider
            get() {
                if (INSTANCE == null) createInstance()
                return INSTANCE!!
            }
    }
}