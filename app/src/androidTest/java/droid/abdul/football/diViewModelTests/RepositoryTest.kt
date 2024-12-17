package droid.abdul.football.diViewModelTests

import droid.abdul.football.repository.Repository
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

class RepositoryTest: KoinTest {

    private val repository: Repository by inject()

    @Before()
    fun before() {
        loadKoinModules(roomTestModule)
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun getCompetition() {
        val competitions = repository.getAllCompetitions().blockingFirst()
        val competitions1 = repository.getAllCompetitions().blockingFirst()
        Assert.assertEquals(competitions, competitions1)
    }
}