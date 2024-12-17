package droid.abdul.football.repository.api

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data classes for Fixtures
 */
@Serializable
data class MatchResponse(
    @SerialName("competition")
    var competition: Competition? = Competition(),
    @SerialName("matches")
    var matches: List<Match>? = ArrayList()
)

@Serializable
data class Match(
    @SerialName("id")
    var id: Long = 0L,
    @SerialName("season")
    var season: Season = Season(),
    @SerialName("utcDate")
    var utcDate: String = "",
    @SerialName("status")
    var status: String = "",
    @SerialName("matchday")
    var matchDay: Int = 0,
    @SerialName("stage")
    var stage: String = "",
    @SerialName("group")
    var group: String = "",
    @SerialName("score")
    var score: Score = Score(),
    @SerialName("homeTeam")
    var homeTeam: TeamData = TeamData(),
    @SerialName("awayTeam")
    var awayTeam: TeamData = TeamData()
)

@Serializable
data class Score(
    @SerialName("winner")
    var winner: String = "",
    @SerialName("duration")
    var duration: String = "",
    @SerialName("fullTime")
    var fullTime: HomeAway = HomeAway(),
    @SerialName("halfTime")
    var halfTime: HomeAway? = HomeAway()
)

@Serializable
data class HomeAway(
    @SerialName("home")
    var home: Int? = 0,
    @SerialName("away")
    var away: Int? = 0
)

@Serializable
data class TeamData(
    @SerialName("id")
    var id: Long = 0L,
    @SerialName("name")
    var name: String = ""
)

/**
 * Data classes for Competitions
 */
@Entity(tableName = "response")
@Serializable
data class CompetitionResponse(
    @PrimaryKey
    var id: Long = 0L,
    @SerialName("competitions")
    var competitions: List<Competition> = ArrayList()
)

@Entity(tableName = "competitions")
@Serializable
data class Competition(
    @PrimaryKey
    @SerialName("id")
    var id: Long = 0L,
    @SerialName("name")
    var name: String = "",
    @SerialName("code")
    var code: String? = "",
    @SerialName("currentSeason")
    var currentSeason: Season? = Season()
)

/**
 * Data classes for Teams
 */
@Serializable
data class TeamResponse(
    @SerialName("id")
    var id: Long = 0L,
    @SerialName("season")
    var season: Season? = Season(),
    @SerialName("teams")
    var teams: List<Team> = ArrayList()
)

@Serializable
data class Team(
    @SerialName("id")
    var id: Int = 0,
    @SerialName("name")
    var name: String = "",
    @SerialName("shortName")
    var shortName: String = "",
    @SerialName("crest")
    var crestUrl: String = ""
)

/**
 * Data classes for Tables/Standings
 */
@Serializable
data class StandingResponse(
    @SerialName("season")
    var season: Season? = Season(),
    @SerialName("standings")
    var standings: List<Standing> = ArrayList()
)

@Serializable
data class Standing(
    @SerialName("type")
    var type: String = "",
    @SerialName("table")
    var table: List<Table> = ArrayList()
)

@Serializable
data class Table(
    @SerialName("position")
    var position: Int = 0,
    @SerialName("team")
    var team: Team = Team(),
    @SerialName("playedGames")
    var played: Int = 0,
    @SerialName("points")
    var points: Int = 0,
    @SerialName("goalDifference")
    var difference: Int = 0
)

/**
 * Data classes for Players in  a Team
 */
@Serializable
data class TeamPlayerResponse(
    @SerialName("crest")
    var crestUrl: String = "",
    @SerialName("squad")
    var squad: List<Player> = ArrayList(),
    @SerialName("name")
    var name: String = "",
    @SerialName("id")
    var id: Int = 0,
    @SerialName("shortName")
    var shortName: String = ""
)

@Serializable
@Parcelize
data class Player(
    @SerialName("role")
    var role: String = "",
    @SerialName("name")
    var name: String = "",
    @SerialName("id")
    var id: Int = 0,
    @SerialName("position")
    var position: String = ""
): Parcelable

/**
 * Common data cases
 */
@Serializable
@Entity(tableName = "seasons")
data class Season(
    @PrimaryKey
    @SerialName("id")
    var id: Long = 0L,
    @SerialName("startDate")
    var startDate: String = "",
    @SerialName("endDate")
    var endDate: String = "",
    @SerialName("currentMatchday")
    var currentMatchday: Int = 0
)

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

@Serializable
data class ErrorResponse(@SerialName("errorCode")
                         var errorCode: Int = 0,
                         @SerialName("message")
                         var message: String = "")