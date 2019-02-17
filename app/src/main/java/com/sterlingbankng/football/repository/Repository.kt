package com.sterlingbankng.football.repository

import android.annotation.SuppressLint
import com.sterlingbankng.football.di.schedulers.SchedulerProvider
import com.sterlingbankng.football.repository.api.*
import com.sterlingbankng.football.repository.local.CompetitionDao
import io.reactivex.Observable

class Repository(
    private val apiService: ApiService,
    private val competitionDao: CompetitionDao,
    private val provider: SchedulerProvider
) {

    fun getAllCompetitions(): Observable<List<Competition>> =
        if (getCompetitionsFromDb().subscribeOn(provider.io()).blockingFirst().isEmpty())
            getCompetitionFromApi()
        else
            getCompetitionsFromDb()


    private fun getCompetitionFromApi(): Observable<List<Competition>> =
        apiService.getAllCompetitions("TIER_ONE")
            .subscribeOn(provider.io())
            .observeOn(provider.io())
            .toObservable()
            .map {
                competitionDao.insertAll(it.competitions)
                it.competitions
            }

    fun getFixturesToday(currentDate: String): Observable<MatchResponse> =
        apiService.getAllFixturesToday(currentDate, currentDate)

    fun getCompetitionTeams(id: Long, season: String): Observable<TeamResponse> =
        apiService.getTeamsByCompetition(id, season)

    fun getCompetitionTable(id: Long): Observable<StandingResponse> =
        apiService.getStandingsByCompetition(id, "TOTAL")

    fun getCompetitionFixtures(id: Long, date: String): Observable<MatchResponse> =
        apiService.getFixturesByCompetition(id, date, date)

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