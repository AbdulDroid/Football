package droid.abdul.football.repository.api.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data classes for Competitions
 */
@Entity(tableName = "response")
@Serializable
data class CompetitionResponseDto(
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