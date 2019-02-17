package com.sterlingbankng.football.di.viewmodels

import androidx.lifecycle.MutableLiveData
import com.sterlingbankng.football.base.BaseViewModel
import com.sterlingbankng.football.di.schedulers.SchedulerProvider
import com.sterlingbankng.football.repository.Repository
import com.sterlingbankng.football.repository.api.*
import java.net.UnknownHostException

class DetailsActivityViewModel(
    private val repository: Repository,
    private val provider: SchedulerProvider
) :
    BaseViewModel() {
    val event = MutableLiveData<LoadingEvent>()
    val detailEvent = MutableLiveData<LoadingEvent>()
    val detailUiData = MutableLiveData<DetailUiData>()
    val teamUiData = MutableLiveData<TeamUiData>()
    val fixtUiData = MutableLiveData<FixtUiData>()
    val tableUiData = MutableLiveData<TableUiData>()

    fun getTeams(id: Long, date: String) {
        fetch {
            event.postValue(LoadingEvent(isLoading = true))
            teamUiData.postValue(TeamUiData())
            repository.getCompetitionTeams(id, date)
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(
                    {
                        if (it.teams.isNotEmpty()) {
                            teamUiData.postValue(
                                TeamUiData(result = it.teams.sortedWith(compareBy { t -> t.name }))
                            )
                            event.postValue(LoadingEvent(isSuccess = true))
                        } else {
                            teamUiData.postValue(TeamUiData(errorMessage = "No Teams"))
                            event.postValue(LoadingEvent(isSuccess = true))
                        }
                    },
                    {
                        event.postValue(LoadingEvent(error = it))
                        if (it is UnknownHostException)
                            teamUiData.postValue(TeamUiData(errorMessage = "No Internet Connection"))
                        else
                            teamUiData.postValue(TeamUiData(errorMessage = it.message!!))
                    }
                )
        }
    }

    fun getTable(id: Long) {
        fetch {
            event.postValue(LoadingEvent(isLoading = true))
            tableUiData.postValue(TableUiData())
            repository.getCompetitionTable(id)
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(
                    {
                        if (it.standings[0].table.isNotEmpty()) {
                            tableUiData.postValue(
                                TableUiData(result = it.standings[0].table.sortedWith(compareBy { t -> t.position }))
                            )
                            event.postValue(LoadingEvent(isSuccess = true))
                        } else {
                            tableUiData.postValue(TableUiData(errorMessage = "No table data"))
                            event.postValue(LoadingEvent(isSuccess = true))
                        }
                    },
                    {
                        event.postValue(LoadingEvent(error = it))
                        tableUiData.postValue(TableUiData(errorMessage = it.message!!))
                    }
                )
        }
    }

    fun getFixtures(id: Long, date: String) {
        fetch {
            event.postValue(LoadingEvent(isLoading = true))
            fixtUiData.postValue(FixtUiData())
            repository.getCompetitionFixtures(id, date)
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(
                    {
                        if (it.matches != null && it.matches?.isNotEmpty() == true) {
                            fixtUiData.postValue(FixtUiData(it.matches!!))
                            event.postValue(LoadingEvent(isSuccess = true))
                        } else {
                            fixtUiData.postValue(FixtUiData(errorMessage = "No Fixtures"))
                            event.postValue(LoadingEvent(isSuccess = true))
                        }
                    },
                    {
                        event.postValue(LoadingEvent(error = it))
                        fixtUiData.postValue(FixtUiData(errorMessage = it.message!!))
                    }
                )
        }
    }

    fun getTeam(id: Long) {
        fetch {
            detailEvent.postValue(LoadingEvent(isLoading = true))
            detailUiData.postValue(DetailUiData())
            repository.getTeamById(id)
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(
                    {
                        if (it != null) {
                            detailUiData.postValue(DetailUiData(result = it))
                            detailEvent.postValue(LoadingEvent(isSuccess = true))
                        } else {
                            detailUiData.postValue(DetailUiData(errorMessage = "No Team data"))
                            detailEvent.postValue(LoadingEvent(isSuccess = true))
                        }
                    },
                    {
                        detailEvent.postValue(LoadingEvent(error = it))
                        it.printStackTrace()
                        detailUiData.postValue(DetailUiData(errorMessage = it.message!!))
                    }
                )
        }
    }
}