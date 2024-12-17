package droid.abdul.football.di.module

import android.content.Context
import droid.abdul.football.di.viewmodels.DetailsActivityViewModel
import droid.abdul.football.di.schedulers.SchedulerProvider
import droid.abdul.football.di.viewmodels.HomeActivityViewModel
import droid.abdul.football.repository.Repository
import droid.abdul.football.repository.api.ApiService
import droid.abdul.football.repository.local.AppDatabase
import droid.abdul.football.repository.local.CompetitionDao
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeActivityViewModel(get(), get()) }
    viewModel { DetailsActivityViewModel(get(), get()) }
    single { createRepository(get(), get(), get ()) }
    single { createDatabase(androidApplication()) }
    single { getCompetitionDao(get()) }
}


fun createRepository(apiService: ApiService, competitionDao: CompetitionDao, provider: SchedulerProvider): Repository {
    return Repository(apiService, competitionDao, provider)
}

fun createDatabase(context: Context): AppDatabase {
    return AppDatabase.getAppDatabase(context)
}

fun getCompetitionDao(appDatabase: AppDatabase): CompetitionDao {
    return appDatabase.competitionDao()
}