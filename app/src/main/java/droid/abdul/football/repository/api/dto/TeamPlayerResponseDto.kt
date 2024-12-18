package droid.abdul.football.repository.api.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data classes for Players in  a Team
 */
@Serializable
data class TeamPlayerResponseDto(
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