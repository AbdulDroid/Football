package droid.abdul.football.diViewModelTests

import droid.abdul.football.repository.Repository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
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
    fun getCompetition() = runTest {
        val competitions = repository.getAllCompetitions().first()
        val competitions1 = repository.getAllCompetitions().first()
        Assert.assertEquals(competitions, competitions1)
    }
}