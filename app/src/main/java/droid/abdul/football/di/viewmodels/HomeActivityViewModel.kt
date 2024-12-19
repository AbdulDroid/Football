package droid.abdul.football.di.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import droid.abdul.football.di.schedulers.DispatcherProvider
import droid.abdul.football.repository.Repository
import droid.abdul.football.repository.api.dto.Competition
import droid.abdul.football.repository.api.dto.Match
import droid.abdul.football.utils.hasInternetConnection
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import droid.abdul.football.ui.models.UiState
import java.net.UnknownHostException

class HomeActivityViewModel(
    private val repository: Repository,
    private val provider: DispatcherProvider,
) : ViewModel() {
    val compUiData = MutableLiveData<UiState<List<Competition>>>()
    val fixUiData = MutableLiveData<UiState<List<Match>>>()

    fun getCompetitions() {
        compUiData.postValue(UiState.Loading)
        viewModelScope.launch {
            val isConnected = hasInternetConnection(provider.io())
            if (!isConnected) {
                compUiData.postValue(UiState.Error(message = "No Internet Connection"))
                return@launch
            }
            repository.getAllCompetitions()
                .catch {
                    it.printStackTrace()
                    if (it !is UnknownHostException)
                        compUiData.postValue(UiState.Error(message = it.message!!))
                    else
                        compUiData.postValue(UiState.Error(message = "No Internet Connection"))
                }
                .onEach {
                    if (it.isNotEmpty()) {
                        compUiData.postValue(
                            UiState.Success(data = it.sortedWith(compareBy { t ->
                                t.name
                            }))
                        )
                    } else {
                        compUiData.postValue(UiState.Error(message = "No competitions"))
                    }
                }.collect()
        }
    }

    fun getTodayFixtures(date: String) {
        viewModelScope.launch {
            fixUiData.postValue(UiState.Loading)
            try {
                val isConnected = hasInternetConnection(provider.io())
                if (!isConnected) {
                    fixUiData.postValue(UiState.Error(message = "No Internet Connection"))
                    return@launch
                }
                val response = repository.getFixturesToday(date)
                if (response.matches != null && response.matches?.isNotEmpty() == true) {
                    fixUiData.postValue(UiState.Success(data = response.matches!!))
                } else {
                    fixUiData.postValue(UiState.Error(message = "No Fixtures"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e !is UnknownHostException)
                    fixUiData.postValue(UiState.Error(message = e.message ?: "An error occurred"))
                else
                    fixUiData.postValue(UiState.Error(message = "No Internet Connection"))
            }
        }
    }
}