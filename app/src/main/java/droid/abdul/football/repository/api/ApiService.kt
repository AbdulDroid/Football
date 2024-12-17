package droid.abdul.football.repository.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("competitions/")
    suspend fun getAllCompetitions(
        @Query("plan") plan: String,
    ): CompetitionResponse

    @GET("competitions/{code}/matches")
    suspend fun getFixturesByCompetition(
        @Path("code") code: String,
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): MatchResponse

    @GET("competitions/{code}/teams")
    suspend fun getTeamsByCompetition(
        @Path("code") code: String,
        @Query("season") season: String
    ): TeamResponse

    @GET("competitions/{code}/standings")
    suspend fun getStandingsByCompetition(
        @Path("code") code: String,
        @Query("standingType") type: String
    ): StandingResponse

    @GET("matches")
    suspend fun getAllFixturesToday(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): MatchResponse

    @GET("teams/{id}")
    suspend fun getTeamById(@Path("id") id: Long): TeamPlayerResponse


    companion object {
        const val BASE_URL = "https://api.football-data.org/v4/"
    }
}