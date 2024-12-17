package droid.abdul.football.di.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import droid.abdul.football.di.schedulers.DispatcherProvider
import droid.abdul.football.repository.Repository
import droid.abdul.football.repository.api.CompUiData
import droid.abdul.football.repository.api.FixUiData
import droid.abdul.football.repository.api.LoadingEvent
import droid.abdul.football.utils.hasInternetConnection
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class HomeActivityViewModel(
    private val repository: Repository,
    private val provider: DispatcherProvider,
) : ViewModel() {
    val event = MutableLiveData<LoadingEvent>()
    val compUiData = MutableLiveData<CompUiData>()
    val fixUiData = MutableLiveData<FixUiData>()

    fun getCompetitions() {
        event.postValue(LoadingEvent(isLoading = true))
        viewModelScope.launch {
            val isConnected = hasInternetConnection(provider.io())
            if (!isConnected) {
                compUiData.postValue(CompUiData(errorMessage = "No Internet Connection"))
                event.postValue(LoadingEvent(isSuccess = true))
                return@launch
            }
            repository.getAllCompetitions()
                .catch {
                    it.printStackTrace()
                    event.postValue(LoadingEvent(error = it))
                    if (it !is UnknownHostException)
                        compUiData.postValue(CompUiData(errorMessage = it.message!!))
                    else
                        compUiData.postValue(CompUiData(errorMessage = "No Internet Connection"))
                }
                .onEach {
                    if (it.isNotEmpty()) {
                        event.postValue(LoadingEvent(isSuccess = true))
                        compUiData.postValue(
                            CompUiData(result = it.sortedWith(compareBy { t ->
                                t.name
                            }))
                        )
                    } else {
                        event.postValue(LoadingEvent(isSuccess = true))
                        compUiData.postValue(CompUiData(errorMessage = "No competitions"))
                    }
                }.collect()
        }
    }

    fun getTodayFixtures(date: String) {
        viewModelScope.launch {
            event.postValue(LoadingEvent(isLoading = true))
            try {
                val isConnected = hasInternetConnection(provider.io())
                if (!isConnected) {
                    fixUiData.postValue(FixUiData(errorMessage = "No Internet Connection"))
                    event.postValue(LoadingEvent(isSuccess = true))
                    return@launch
                }
                val response = repository.getFixturesToday(date)
                if (response.matches != null && response.matches?.isNotEmpty() == true) {
                    fixUiData.postValue(FixUiData(result = response.matches!!))
                    event.postValue(LoadingEvent(isSuccess = true))
                } else {
                    fixUiData.postValue(FixUiData(errorMessage = "No Fixtures"))
                    event.postValue(LoadingEvent(isSuccess = true))
                }
            } catch (e: Exception) {
                event.postValue(LoadingEvent(error = e))
                e.printStackTrace()
                if (e !is UnknownHostException)
                    fixUiData.postValue(FixUiData(errorMessage = e.message ?: "An error occurred"))
                else
                    fixUiData.postValue(FixUiData(errorMessage = "No Internet Connection"))
            }
        }
    }
}