package droid.abdul.football.repository.api

import droid.abdul.football.repository.api.dto.CompetitionResponseDto
import droid.abdul.football.repository.api.dto.MatchResponseDto
import droid.abdul.football.repository.api.dto.StandingResponseDto
import droid.abdul.football.repository.api.dto.TeamPlayerResponseDto
import droid.abdul.football.repository.api.dto.TeamResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("competitions/")
    suspend fun getAllCompetitions(
        @Query("plan") plan: String,
    ): CompetitionResponseDto

    @GET("competitions/{code}/matches")
    suspend fun getFixturesByCompetition(
        @Path("code") code: String,
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): MatchResponseDto

    @GET("competitions/{code}/teams")
    suspend fun getTeamsByCompetition(
        @Path("code") code: String,
        @Query("season") season: String
    ): TeamResponseDto

    @GET("competitions/{code}/standings")
    suspend fun getStandingsByCompetition(
        @Path("code") code: String,
        @Query("standingType") type: String
    ): StandingResponseDto

    @GET("matches")
    suspend fun getAllFixturesToday(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String
    ): MatchResponseDto

    @GET("teams/{id}")
    suspend fun getTeamById(@Path("id") id: Long): TeamPlayerResponseDto


    companion object {
        const val BASE_URL = "https://api.football-data.org/v4/"
    }
}