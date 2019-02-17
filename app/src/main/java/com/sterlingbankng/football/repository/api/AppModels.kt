package com.sterlingbankng.football.repository.api

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Data classes for Fixtures
 */
data class MatchResponse(
    @SerializedName("competition")
    var competition: Competition? = Competition(),
    @SerializedName("matches")
    var matches: List<Match>? = ArrayList()
)

data class Match(
    @SerializedName("id")
    var id: Long = 0L,
    @SerializedName("season")
    var season: Season = Season(),
    @SerializedName("utcDate")
    var utcDate: String = "",
    @SerializedName("status")
    var status: String = "",
    @SerializedName("matchday")
    var matchDay: Int = 0,
    @SerializedName("stage")
    var stage: String = "",
    @SerializedName("group")
    var group: String = "",
    @SerializedName("score")
    var score: Score = Score(),
    @SerializedName("homeTeam")
    var homeTeam: TeamData = TeamData(),
    @SerializedName("awayTeam")
    var awayTeam: TeamData = TeamData()
)

data class Score(
    @SerializedName("winner")
    var winner: String = "",
    @SerializedName("duration")
    var duration: String = "",
    @SerializedName("fullTime")
    var fullTime: HomeAway = HomeAway(),
    @SerializedName("halfTime")
    var halfTime: HomeAway? = HomeAway()
)

data class HomeAway(
    @SerializedName("homeTeam")
    var home: Int? = 0,
    @SerializedName("awayTeam")
    var away: Int? = 0
)

data class TeamData(
    @SerializedName("id")
    var id: Long = 0L,
    @SerializedName("name")
    var name: String = ""
)

/**
 * Data classes for Competitions
 */
@Entity(tableName = "response")
data class CompetitionResponse(
    @PrimaryKey
    var id: Long = 0L,
    @SerializedName("competitions")
    var competitions: List<Competition> = ArrayList()
)

@Entity(tableName = "competitions")
@Parcelize
data class Competition(
    @PrimaryKey
    @SerializedName("id")
    var id: Long = 0L,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("currentSeason")
    var currentSeason: Season = Season()
) : Parcelable

/**
 * Data classes for Teams
 */
data class TeamResponse(
    @SerializedName("id")
    var id: Long = 0L,
    @SerializedName("season")
    var season: Season? = Season(),
    @SerializedName("teams")
    var teams: List<Team> = ArrayList()
)

data class Team(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("shortName")
    var shortName: String = "",
    @SerializedName("crestUrl")
    var crestUrl: String = ""

)

/**
 * Data classes for Tables/Standings
 */
data class StandingResponse(
    @SerializedName("season")
    var season: Season? = Season(),
    @SerializedName("standings")
    var standings: List<Standing> = ArrayList()
)

data class Standing(
    @SerializedName("type")
    var type: String = "",
    @SerializedName("table")
    var table: List<Table> = ArrayList()
)

data class Table(
    @SerializedName("position")
    var position: Int = 0,
    @SerializedName("team")
    var team: Team = Team(),
    @SerializedName("playedGames")
    var played: Int = 0,
    @SerializedName("points")
    var points: Int = 0,
    @SerializedName("goalDifference")
    var difference: Int = 0
)

/**
 * Data classes for Players in  a Team
 */
data class TeamPlayerResponse(
    @SerializedName("crestUrl")
    var crestUrl: String = "",
    @SerializedName("squad")
    var squad: List<Player> = ArrayList(),
    @SerializedName("name")
    var name: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("shortName")
    var shortName: String = ""
)

@Parcelize
data class Player(
    @SerializedName("role")
    var role: String = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("position")
    var position: String = ""
): Parcelable

/**
 * Common data cases
 */
@Parcelize
@Entity(tableName = "seasons")
data class Season(
    @PrimaryKey
    @SerializedName("id")
    var id: Long = 0L,
    @SerializedName("startDate")
    var startDate: String = "",
    @SerializedName("endDate")
    var endDate: String = "",
    @SerializedName("currentMatchday")
    var currentMatchday: Int = 0
) : Parcelable

/**
 * Data classes sed for view updates
 */
data class LoadingEvent(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: Throwable? = null
)

data class CompUiData(
    var result: List<Competition> = ArrayList(),
    var errorMessage: String = ""
)

data class FixUiData(
    var result: List<Match> = ArrayList(),
    var errorMessage: String = ""
)

data class TeamUiData(
    var result: List<Team> = ArrayList(),
    var errorMessage: String = ""
)

data class TableUiData(
    var result: List<Table> = ArrayList(),
    var errorMessage: String = ""
)

data class DetailUiData(
    var result: TeamPlayerResponse? = null,
    var errorMessage: String = ""
)

data class FixtUiData(
    var result: List<Match> = ArrayList(),
    var errorMessage: String = ""
)

data class ErrorResponse(@SerializedName("errorCode")
                         var errorCode: Int = 0,
                         @SerializedName("message")
                         var message: String = "")