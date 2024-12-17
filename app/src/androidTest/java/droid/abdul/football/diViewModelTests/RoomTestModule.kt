package droid.abdul.football.diViewModelTests

import androidx.room.Room
import droid.abdul.football.repository.local.AppDatabase
import org.koin.dsl.module

val roomTestModule = module {
    single {
        Room.inMemoryDatabaseBuilder(get(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}