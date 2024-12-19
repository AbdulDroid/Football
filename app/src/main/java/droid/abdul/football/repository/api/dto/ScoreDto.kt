package droid.abdul.football.repository.api.dto

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