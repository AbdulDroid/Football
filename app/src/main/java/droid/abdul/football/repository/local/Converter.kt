package droid.abdul.football.repository.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import droid.abdul.football.repository.api.Competition
import droid.abdul.football.repository.api.Season

class Converter{
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromSeason(value: Season?): String{
            return Gson().toJson(value)
        }

        @TypeConverter
        @JvmStatic
        fun toSeason(value: String?): Season? {
            return Gson().fromJson(value, Season::class.java)
        }
        @TypeConverter
        @JvmStatic
        fun fromCompetitions(value: List<Competition>): String {
            return Gson().toJson(value)
        }
        @TypeConverter
        @JvmStatic
        fun toCompetitions(value: String): List<Competition> {
            return Gson().fromJson(value, Array<Competition>::class.java).asList()
        }
    }
}