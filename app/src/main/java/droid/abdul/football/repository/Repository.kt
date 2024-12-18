package droid.abdul.football.repository

import android.annotation.SuppressLint
import android.util.Log
import droid.abdul.football.di.schedulers.DispatcherProvider
import droid.abdul.football.repository.api.ApiService
import droid.abdul.football.repository.api.dto.Competition
import droid.abdul.football.repository.api.dto.MatchResponseDto
import droid.abdul.football.repository.api.dto.StandingResponseDto
import droid.abdul.football.repository.api.dto.TeamPlayerResponseDto
import droid.abdul.football.repository.api.dto.TeamResponseDto
import droid.abdul.football.repository.local.CompetitionDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class Repository(
    private val apiService: ApiService,
    private val competitionDao: CompetitionDao,
    private val provider: DispatcherProvider
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllCompetitions(): Flow<List<Competition>> =
        getCompetitionsFromDb()
            .catch { Log.e("Repository", it.toString()) }
            .flowOn(provider.ui())
            .flatMapLatest {
                if (it.isEmpty()) {
                    getCompetitionFromApi()
                } else {
                    flowOf(it)
                }
            }


    private fun getCompetitionFromApi(): Flow<List<Competition>> = flow {
        apiService.getAllCompetitions("TIER_ONE")
            .also {
                CoroutineScope(provider.io()).launch {
                    competitionDao.insertAll(it.competitions.filter { comp -> comp.currentSeason != null })
                }
                emit(it.competitions)
            }
    }

    suspend fun getFixturesToday(currentDate: String): MatchResponseDto =
        apiService.getAllFixturesToday(currentDate, currentDate)

    fun getCompetitionTeams(code: String, season: String): Flow<TeamResponseDto> = flow {
        emit(apiService.getTeamsByCompetition(code, season))
    }

    fun getCompetitionTable(code: String): Flow<StandingResponseDto> = flow {
        emit(apiService.getStandingsByCompetition(code, "TOTAL"))
    }

    fun getCompetitionFixtures(code: String, date: String): Flow<MatchResponseDto> = flow {
        emit(apiService.getFixturesByCompetition(code, date, date))
    }

    suspend fun getTeamById(id: Long): TeamPlayerResponseDto =
        apiService.getTeamById(id)

    @SuppressLint("CheckResult")
    private fun getCompetitionsFromDb(): Flow<List<Competition>> {
        return competitionDao.getCompetitions().onEach {
            if (it.isEmpty()) {
                getCompetitionFromApi()
            }
        }
    }
}