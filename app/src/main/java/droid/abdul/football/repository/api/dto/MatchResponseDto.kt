package droid.abdul.football.repository.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data classes for Fixtures
 */
@Serializable
data class MatchResponseDto(
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