package droid.abdul.football.di.module

import android.content.Context
import droid.abdul.football.di.viewmodels.DetailsActivityViewModel
import droid.abdul.football.di.schedulers.DispatcherProvider
import droid.abdul.football.di.viewmodels.HomeActivityViewModel
import droid.abdul.football.repository.Repository
import droid.abdul.football.repository.api.ApiService
import droid.abdul.football.repository.local.AppDatabase
import droid.abdul.football.repository.local.CompetitionDao
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeActivityViewModel)
    viewModelOf(::DetailsActivityViewModel)
    single { createRepository(get(), get(), get ()) }
    singleOf(::createDatabase)
    singleOf(::getCompetitionDao)
}


fun createRepository(apiService: ApiService, competitionDao: CompetitionDao, provider: DispatcherProvider): Repository {
    return Repository(apiService, competitionDao, provider)
}

fun createDatabase(context: Context): AppDatabase {
    return AppDatabase.getAppDatabase(context)
}

fun getCompetitionDao(appDatabase: AppDatabase): CompetitionDao {
    return appDatabase.competitionDao()
}