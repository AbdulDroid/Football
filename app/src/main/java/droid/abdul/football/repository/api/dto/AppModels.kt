package droid.abdul.football.repository.api.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable





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
    var result: TeamPlayerResponseDto? = null,
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