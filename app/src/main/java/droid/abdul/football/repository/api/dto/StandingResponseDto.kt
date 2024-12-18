package droid.abdul.football.repository.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data classes for Tables/Standings
 */
@Serializable
data class StandingResponseDto(
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