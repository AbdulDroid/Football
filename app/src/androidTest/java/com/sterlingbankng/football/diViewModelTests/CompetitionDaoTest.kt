package com.sterlingbankng.football.diViewModelTests

import com.sterlingbankng.football.repository.Repository
import com.sterlingbankng.football.repository.api.Competition
import com.sterlingbankng.football.repository.local.AppDatabase
import com.sterlingbankng.football.repository.local.CompetitionDao
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