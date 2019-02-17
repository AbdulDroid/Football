package com.sterlingbankng.football.diViewModelTests

import androidx.room.Room
import com.sterlingbankng.football.repository.local.AppDatabase
import org.koin.dsl.module

val roomTestModule = module(override = true) {
    single {
        Room.inMemoryDatabaseBuilder(get(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}