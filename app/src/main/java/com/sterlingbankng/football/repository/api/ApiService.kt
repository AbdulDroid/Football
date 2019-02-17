package com.sterlingbankng.football.repository.api

import com.sterlingbankng.football.BuildConfig
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("competitions")
    @Headers("X-Auth-Token: ${BuildConfig.X_AUTH_TOKEN}")
    fun getAllCompetitions(
        @Query("plan") plan: String
    ): Single<CompetitionResponse>

    @GET("competitions/{id}/matches")
    @Headers("X-Auth-Token: ${BuildConfig.X_AUTH_TOKEN}")
    fun getFixturesByCompetition(
        @Path("id") id: Long,
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): Observable<MatchResponse>

    @GET("competitions/{id}/teams")
    @Headers("X-Auth-Token: ${BuildConfig.X_AUTH_TOKEN}")
    fun getTeamsByCompetition(
        @Path("id") di: Long,
        @Query("season") season: String
    ): Observable<TeamResponse>

    @GET("competitions/{id}/standings")
    @Headers("X-Auth-Token: ${BuildConfig.X_AUTH_TOKEN}")
    fun getStandingsByCompetition(
        @Path("id") id: Long,
        @Query("standingType") type: String
    ): Observable<StandingResponse>

    @GET("matches")
    @Headers("X-Auth-Token: ${BuildConfig.X_AUTH_TOKEN}")
    fun getAllFixturesToday(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): Observable<MatchResponse>

    @GET("teams/{id}")
    @Headers("X-Auth-Token: ${BuildConfig.X_AUTH_TOKEN}")
    fun getTeamById(@Path("id") id: Long): Observable<TeamPlayerResponse>


    companion object {
        const val BASE_URL = "https://api.football-data.org/v2/"
    }
}