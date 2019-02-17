package com.sterlingbankng.football.di.viewmodels

import androidx.lifecycle.MutableLiveData
import com.sterlingbankng.football.base.BaseViewModel
import com.sterlingbankng.football.di.schedulers.SchedulerProvider
import com.sterlingbankng.football.repository.Repository
import com.sterlingbankng.football.repository.api.CompUiData
import com.sterlingbankng.football.repository.api.FixUiData
import com.sterlingbankng.football.repository.api.LoadingEvent
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class HomeActivityViewModel(
    private val repository: Repository,
    private val provider: SchedulerProvider
) :
    BaseViewModel() {
    val event = MutableLiveData<LoadingEvent>()
    val compUiData = MutableLiveData<CompUiData>()
    val fixUiData = MutableLiveData<FixUiData>()

    fun getCompetitions() {
        fetch {
            event.postValue(LoadingEvent(isLoading = true))
            repository.getAllCompetitions()
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(
                    {
                        //Log.e("TEST", it.toString())
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
                    }, {
                        it.printStackTrace()
                        event.postValue(LoadingEvent(error = it))
                        if (it !is UnknownHostException)
                        compUiData.postValue(CompUiData(errorMessage = it.message!!))
                        else
                            compUiData.postValue(CompUiData(errorMessage = "No Internet Connection"))
                    })
        }
    }

    fun getTodayFixtures(date: String) {
        fetch {
            event.postValue(LoadingEvent(isLoading = true))
            repository.getFixturesToday(date)
                .subscribeOn(provider.io())
                .observeOn(provider.ui())
                .subscribe(
                    {
                        if (it.matches != null && it.matches?.isNotEmpty() == true) {
                            fixUiData.postValue(FixUiData(result = it.matches!!))
                            event.postValue(LoadingEvent(isSuccess = true))
                        } else {
                            fixUiData.postValue(FixUiData(errorMessage = "No Fixtures"))
                            event.postValue(LoadingEvent(isSuccess = true))
                        }
                    },
                    {
                        event.postValue(LoadingEvent(error = it))
                        it.printStackTrace()
                        if (it !is UnknownHostException)
                            fixUiData.postValue(FixUiData(errorMessage = it.message!!))
                        else
                            fixUiData.postValue(FixUiData(errorMessage = "No Internet Connection"))
                    })
        }
    }
}