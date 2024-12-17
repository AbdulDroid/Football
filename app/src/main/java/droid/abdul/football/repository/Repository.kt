package droid.abdul.football.repository

import android.annotation.SuppressLint
import android.util.Log
import droid.abdul.football.di.schedulers.DispatcherProvider
import droid.abdul.football.repository.api.ApiService
import droid.abdul.football.repository.api.Competition
import droid.abdul.football.repository.api.MatchResponse
import droid.abdul.football.repository.api.StandingResponse
import droid.abdul.football.repository.api.TeamPlayerResponse
import droid.abdul.football.repository.api.TeamResponse
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

    suspend fun getFixturesToday(currentDate: String): MatchResponse =
        apiService.getAllFixturesToday(currentDate, currentDate)

    fun getCompetitionTeams(code: String, season: String): Flow<TeamResponse> = flow {
        emit(apiService.getTeamsByCompetition(code, season))
    }

    fun getCompetitionTable(code: String): Flow<StandingResponse> = flow {
        emit(apiService.getStandingsByCompetition(code, "TOTAL"))
    }

    fun getCompetitionFixtures(code: String, date: String): Flow<MatchResponse> = flow {
        emit(apiService.getFixturesByCompetition(code, date, date))
    }

    suspend fun getTeamById(id: Long): TeamPlayerResponse =
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