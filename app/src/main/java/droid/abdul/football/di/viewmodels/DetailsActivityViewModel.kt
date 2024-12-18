package droid.abdul.football.di.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import droid.abdul.football.di.schedulers.DispatcherProvider
import droid.abdul.football.repository.Repository
import droid.abdul.football.repository.api.dto.DetailUiData
import droid.abdul.football.repository.api.dto.FixtUiData
import droid.abdul.football.repository.api.dto.LoadingEvent
import droid.abdul.football.repository.api.dto.TableUiData
import droid.abdul.football.repository.api.dto.TeamUiData
import droid.abdul.football.utils.hasInternetConnection
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class DetailsActivityViewModel(
    private val repository: Repository,
    private val provider: DispatcherProvider
) : ViewModel() {
    val event = MutableLiveData<LoadingEvent>()
    val detailEvent = MutableLiveData<LoadingEvent>()
    val detailUiData = MutableLiveData<DetailUiData>()
    val teamUiData = MutableLiveData<TeamUiData>()
    val fixtUiData = MutableLiveData<FixtUiData>()
    val tableUiData = MutableLiveData<TableUiData>()

    fun getTeams(code: String, date: String) {
        event.postValue(LoadingEvent(isLoading = true))
        teamUiData.postValue(TeamUiData())
        viewModelScope.launch {
            val isConnected = hasInternetConnection(provider.io())
            if (!isConnected) {
                teamUiData.postValue(TeamUiData(errorMessage = "No Internet Connection"))
                event.postValue(LoadingEvent(isSuccess = true))
                return@launch
            }
            repository.getCompetitionTeams(code, date)
                .catch {
                    event.postValue(LoadingEvent(error = it))
                    if (it is UnknownHostException)
                        teamUiData.postValue(TeamUiData(errorMessage = "No Internet Connection"))
                    else
                        teamUiData.postValue(TeamUiData(errorMessage = it.message!!))
                }
                .onEach {
                    if (it.teams.isNotEmpty()) {
                        teamUiData.postValue(
                            TeamUiData(result = it.teams.sortedWith(compareBy { t -> t.name }))
                        )
                        event.postValue(LoadingEvent(isSuccess = true))
                    } else {
                        teamUiData.postValue(TeamUiData(errorMessage = "No Teams"))
                        event.postValue(LoadingEvent(isSuccess = true))
                    }
                }.collect()
        }
    }

    fun getTable(code: String) {
        event.postValue(LoadingEvent(isLoading = true))
        tableUiData.postValue(TableUiData())
        viewModelScope.launch {
            val isConnected = hasInternetConnection(provider.io())
            if (!isConnected) {
                tableUiData.postValue(TableUiData(errorMessage = "No Internet Connection"))
                event.postValue(LoadingEvent(isSuccess = true))
            }
            repository.getCompetitionTable(code)
                .catch {
                    event.postValue(LoadingEvent(error = it))
                    tableUiData.postValue(TableUiData(errorMessage = it.message!!))
                }
                .onEach {
                    if (it.standings[0].table.isNotEmpty()) {
                        tableUiData.postValue(
                            TableUiData(result = it.standings[0].table.sortedWith(compareBy { t -> t.position }))
                        )
                        event.postValue(LoadingEvent(isSuccess = true))
                    } else {
                        tableUiData.postValue(TableUiData(errorMessage = "No table data"))
                        event.postValue(LoadingEvent(isSuccess = true))
                    }
                }.collect()
        }
    }

    fun getFixtures(code: String, date: String) {
        event.postValue(LoadingEvent(isLoading = true))
        fixtUiData.postValue(FixtUiData())
        viewModelScope.launch {
            val isConnected = hasInternetConnection(provider.io())
            if (!isConnected) {
                fixtUiData.postValue(FixtUiData(errorMessage = "No Internet Connection"))
                detailEvent.postValue(LoadingEvent(isSuccess = true))
                return@launch
            }
            repository.getCompetitionFixtures(code, date)
                .catch {
                    event.postValue(LoadingEvent(error = it))
                    fixtUiData.postValue(FixtUiData(errorMessage = it.message!!))
                }
                .onEach {
                    if (it.matches != null && it.matches?.isNotEmpty() == true) {
                        fixtUiData.postValue(FixtUiData(it.matches!!))
                        event.postValue(LoadingEvent(isSuccess = true))
                    } else {
                        fixtUiData.postValue(FixtUiData(errorMessage = "No Fixtures"))
                        event.postValue(LoadingEvent(isSuccess = true))
                    }
                }.collect()
        }
    }

    fun getTeam(id: Long) {
        viewModelScope.launch {
            detailEvent.postValue(LoadingEvent(isLoading = true))
            detailUiData.postValue(DetailUiData())
            try {
                val isConnected = hasInternetConnection(provider.io())
                if (!isConnected) {
                    detailUiData.postValue(DetailUiData(errorMessage = "No Internet Connection"))
                    detailEvent.postValue(LoadingEvent(isSuccess = true))
                    return@launch
                }
                val response = repository.getTeamById(id)
                if (response != null) {
                    detailUiData.postValue(DetailUiData(result = response))
                    detailEvent.postValue(LoadingEvent(isSuccess = true))
                } else {
                    detailUiData.postValue(DetailUiData(errorMessage = "No Team data"))
                    detailEvent.postValue(LoadingEvent(isSuccess = true))
                }
            } catch (e: Exception) {
                detailEvent.postValue(LoadingEvent(error = e))
                e.printStackTrace()
                detailUiData.postValue(DetailUiData(errorMessage = e.message!!))
            }
        }
    }
}