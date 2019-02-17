package com.sterlingbankng.football.di.module

import android.content.Context
import com.sterlingbankng.football.di.viewmodels.DetailsActivityViewModel
import com.sterlingbankng.football.di.schedulers.SchedulerProvider
import com.sterlingbankng.football.di.viewmodels.HomeActivityViewModel
import com.sterlingbankng.football.repository.Repository
import com.sterlingbankng.football.repository.api.ApiService
import com.sterlingbankng.football.repository.local.AppDatabase
import com.sterlingbankng.football.repository.local.CompetitionDao
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
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