package com.sterlingbankng.football.repository

import android.annotation.SuppressLint
import android.util.Log
import com.sterlingbankng.football.di.schedulers.SchedulerProvider
import com.sterlingbankng.football.repository.api.ApiService
import com.sterlingbankng.football.repository.api.Competition
import com.sterlingbankng.football.repository.api.MatchResponse
import com.sterlingbankng.football.repository.api.StandingResponse
import com.sterlingbankng.football.repository.api.TeamPlayerResponse
import com.sterlingbankng.football.repository.api.TeamResponse
import com.sterlingbankng.football.repository.local.CompetitionDao
import io.reactivex.Observable

class Repository(
    private val apiService: ApiService,
    private val competitionDao: CompetitionDao,
    private val provider: SchedulerProvider
) {

    fun getAllCompetitions(): Observable<List<Competition>> =
        if (getCompetitionsFromDb()
                .subscribeOn(provider.io())
                .doOnError {
                    Log.e("Repository", it.toString())
                }
                .blockingFirst().isEmpty()
        )
            getCompetitionFromApi()
        else
            getCompetitionsFromDb()


    private fun getCompetitionFromApi(): Observable<List<Competition>> =
        apiService.getAllCompetitions("TIER_ONE")
            .subscribeOn(provider.io())
            .observeOn(provider.io())
            .toObservable()
            .map {
                competitionDao.insertAll(it.competitions.filter { it.currentSeason != null })
                it.competitions
            }

    fun getFixturesToday(currentDate: String): Observable<MatchResponse> =
        apiService.getAllFixturesToday(currentDate, currentDate)

    fun getCompetitionTeams(code: String, season: String): Observable<TeamResponse> =
        apiService.getTeamsByCompetition(code, season)

    fun getCompetitionTable(code: String): Observable<StandingResponse> =
        apiService.getStandingsByCompetition(code, "TOTAL")

    fun getCompetitionFixtures(code: String, date: String): Observable<MatchResponse> =
        apiService.getFixturesByCompetition(code, date, date)

    fun getTeamById(id: Long): Observable<TeamPlayerResponse> =
        apiService.getTeamById(id)

    @SuppressLint("CheckResult")
    private fun getCompetitionsFromDb(): Observable<List<Competition>> {
        return competitionDao.getCompetitions().doOnNext {
            if (it == null)
                getCompetitionFromApi()
        }
    }

    /*private fun getCompetitionsFromDb(): Observable<Competition> {
        //val competition: ArrayList<Competition>() =competitionDao.getCompetitions()
        val competitions = competitionDao.getCompetitions()
                competitions.subscribeOn(provider.io())
            .observeOn(provider.io())
            .doOnNext {
                Log.e("Data", it.toString())
                //competitions.addAll(it)
            }
        //Log.e("CompetitionsDB", competitions.toString())
        return Observable.create{competitions}
        //Observable.just(CompetitionResponse(competitions))
    }*/
}