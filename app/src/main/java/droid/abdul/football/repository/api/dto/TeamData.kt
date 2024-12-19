package droid.abdul.football.repository.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamData(
    @SerialName("id")
    var id: Long = 0L,
    @SerialName("name")
    var name: String = ""
)