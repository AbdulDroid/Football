package droid.abdul.football.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import droid.abdul.football.repository.api.Competition
import droid.abdul.football.repository.api.CompetitionResponse
import droid.abdul.football.repository.api.Season


@Database(entities = [CompetitionResponse::class, Competition::class, Season::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun competitionDao(): CompetitionDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getAppDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "football")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }
    }
}