package com.sterlingbankng.football.di.schedulers

import io.reactivex.Scheduler

interface BaseScheduler {
    fun computation(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler
}