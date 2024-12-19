package droid.abdul.football.repository.api.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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