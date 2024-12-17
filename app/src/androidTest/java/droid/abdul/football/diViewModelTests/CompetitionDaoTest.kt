package droid.abdul.football.diViewModelTests

import droid.abdul.football.repository.Repository
import droid.abdul.football.repository.api.Competition
import droid.abdul.football.repository.local.AppDatabase
import droid.abdul.football.repository.local.CompetitionDao
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class CompetitionDaoTest: KoinTest {

    private val appDatabase: AppDatabase by inject()
    private val competitionDao: CompetitionDao by inject()
    private val repository: Repository by inject()

    @Before
    fun before() {
        loadKoinModules(roomTestModule)
    }

    @After
    fun after() {
        appDatabase.close()
        stopKoin()
    }

    @Test
    fun testReadWrite() {
        val competitions = getCompetitionsAsEntities()
        competitionDao.insertAll(competitions)
        val ids = competitions.map { it.id }
        val requestedCompetitions = ids.map { competitionDao.getCompetitionById(it).blockingFirst() }
        Assert.assertEquals(competitions, requestedCompetitions)
    }

    private fun getCompetitionsAsEntities(): List<Competition> {
        return repository.getAllCompetitions()
            .blockingFirst()
    }
}