package droid.abdul.football.repository.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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