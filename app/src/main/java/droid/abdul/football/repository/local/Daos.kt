package droid.abdul.football.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import droid.abdul.football.repository.api.Competition
import io.reactivex.Observable


@Dao
interface CompetitionDao {
    @Query("SELECT * FROM competitions")
    fun getCompetitions(): Observable<List<Competition>>

    @Query("SELECT * FROM competitions WHERE id=:id")
    fun getCompetitionById(id: Long): Observable<Competition>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(competition: Competition)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(competitions: List<Competition>)
}
