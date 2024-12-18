package droid.abdul.football.repository.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data classes for Teams
 */
@Serializable
data class TeamResponseDto(
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