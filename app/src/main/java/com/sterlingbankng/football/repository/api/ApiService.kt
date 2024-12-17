package com.sterlingbankng.football.repository.api

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("competitions/")
    fun getAllCompetitions(
        @Query("plan") plan: String,
    ): Single<CompetitionResponse>

    @GET("competitions/{code}/matches")
    fun getFixturesByCompetition(
        @Path("code") code: String,
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): Observable<MatchResponse>

    @GET("competitions/{code}/teams")
    fun getTeamsByCompetition(
        @Path("code") code: String,
        @Query("season") season: String
    ): Observable<TeamResponse>

    @GET("competitions/{code}/standings")
    fun getStandingsByCompetition(
        @Path("code") code: String,
        @Query("standingType") type: String
    ): Observable<StandingResponse>

    @GET("matches")
    fun getAllFixturesToday(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): Observable<MatchResponse>

    @GET("teams/{id}")
    fun getTeamById(@Path("id") id: Long): Observable<TeamPlayerResponse>


    companion object {
        const val BASE_URL = "https://api.football-data.org/v4/"
    }
}