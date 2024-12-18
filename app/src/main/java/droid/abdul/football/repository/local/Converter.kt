package droid.abdul.football.repository.local

import androidx.room.TypeConverter
import droid.abdul.football.repository.api.dto.Competition
import droid.abdul.football.repository.api.dto.Season
import kotlinx.serialization.json.Json

class Converter{
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromSeason(value: Season?): String{
            return Json.encodeToString(value)
        }

        @TypeConverter
        @JvmStatic
        fun toSeason(value: String?): Season? {
            return Json.decodeFromString<Season?>(value ?: "")
        }
        @TypeConverter
        @JvmStatic
        fun fromCompetitions(value: List<Competition>): String {
            return Json.encodeToString(value)
        }
        @TypeConverter
        @JvmStatic
        fun toCompetitions(value: String): List<Competition> {
            return Json.decodeFromString<List<Competition>>(value)
        }
    }
}