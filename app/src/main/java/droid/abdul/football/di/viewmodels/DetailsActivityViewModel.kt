package droid.abdul.football.di.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import droid.abdul.football.di.schedulers.DispatcherProvider
import droid.abdul.football.repository.Repository
import droid.abdul.football.repository.api.dto.Match
import droid.abdul.football.repository.api.dto.Table
import droid.abdul.football.repository.api.dto.Team
import droid.abdul.football.repository.api.dto.TeamPlayerResponseDto
import droid.abdul.football.ui.models.UiState
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
    val detailUiData = MutableLiveData<UiState<TeamPlayerResponseDto>>()
    val teamUiData = MutableLiveData<UiState<List<Team>>>()
    val fixtUiData = MutableLiveData<UiState<List<Match>>>()
    val tableUiData = MutableLiveData<UiState<List<Table>>>()

    fun getTeams(code: String, date: String) {
        teamUiData.postValue(UiState.Loading)
        viewModelScope.launch {
            val isConnected = hasInternetConnection(provider.io())
            if (!isConnected) {
                teamUiData.postValue(UiState.Error(message = "No Internet Connection"))
                return@launch
            }
            repository.getCompetitionTeams(code, date)
                .catch {
                    if (it is UnknownHostException)
                        teamUiData.postValue(UiState.Error(message = "No Internet Connection"))
                    else
                        teamUiData.postValue(UiState.Error(message = it.message!!))
                }
                .onEach {
                    if (it.teams.isNotEmpty()) {
                        teamUiData.postValue(
                            UiState.Success(data = it.teams.sortedWith(compareBy { t -> t.name }))
                        )
                    } else {
                        teamUiData.postValue(UiState.Error(message = "No Teams"))
                    }
                }.collect()
        }
    }

    fun getTable(code: String) {
        tableUiData.postValue(UiState.Loading)
        viewModelScope.launch {
            val isConnected = hasInternetConnection(provider.io())
            if (!isConnected) {
                tableUiData.postValue(UiState.Error(message = "No Internet Connection"))
            }
            repository.getCompetitionTable(code)
                .catch {
                    tableUiData.postValue(UiState.Error(message = it.message!!))
                }
                .onEach {
                    if (it.standings[0].table.isNotEmpty()) {
                        tableUiData.postValue(
                            UiState.Success(data = it.standings[0].table.sortedWith(compareBy { t -> t.position }))
                        )
                    } else {
                        tableUiData.postValue(UiState.Error(message = "No table data"))
                    }
                }.collect()
        }
    }

    fun getFixtures(code: String, date: String) {
        fixtUiData.postValue(UiState.Loading)
        viewModelScope.launch {
            val isConnected = hasInternetConnection(provider.io())
            if (!isConnected) {
                fixtUiData.postValue(UiState.Error(message = "No Internet Connection"))
                return@launch
            }
            repository.getCompetitionFixtures(code, date)
                .catch {
                    fixtUiData.postValue(UiState.Error(message = it.message!!))
                }
                .onEach {
                    if (it.matches != null && it.matches?.isNotEmpty() == true) {
                        fixtUiData.postValue(UiState.Success(data = it.matches!!))
                    } else {
                        fixtUiData.postValue(UiState.Error(message = "No Fixtures"))
                    }
                }.collect()
        }
    }

    fun getTeam(id: Long) {
        viewModelScope.launch {
            detailUiData.postValue(UiState.Loading)
            try {
                val isConnected = hasInternetConnection(provider.io())
                if (!isConnected) {
                    detailUiData.postValue(UiState.Error(message = "No Internet Connection"))
                    return@launch
                }
                val response = repository.getTeamById(id)
                if (response != null) {
                    detailUiData.postValue(UiState.Success(data = response))
                } else {
                    detailUiData.postValue(UiState.Error(message = "No Team data"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                detailUiData.postValue(UiState.Error(message = e.message!!))
            }
        }
    }
}