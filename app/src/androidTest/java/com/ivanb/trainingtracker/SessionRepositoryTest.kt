package com.ivanb.trainingtracker

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ivanb.trainingtracker.data.AppDatabase
import com.ivanb.trainingtracker.data.CatalogExercise
import com.ivanb.trainingtracker.data.LoggedSet
import com.ivanb.trainingtracker.data.SessionDao
import com.ivanb.trainingtracker.data.SessionRepository
import com.ivanb.trainingtracker.data.WorkoutSession
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

class SessionRepositoryTest {

    @RunWith(AndroidJUnit4::class)
    class SessionRepositoryTest {
        private lateinit var db: AppDatabase
        private lateinit var dao: SessionDao
        private lateinit var repository: SessionRepository

        @Before
        fun setUp() {
            db = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                AppDatabase::class.java
            ).build()
            dao = db.sessionDao()
            repository = SessionRepository(dao)
        }

        @After
        fun tearDown() {
            db.close()
        }

        @Test
        fun startOrResume_returnsSameSession_whileOneIsUnfinished() = runBlocking {
            val first = repository.startOrResume()
            val second = repository.startOrResume()

            assertEquals(first, second)
        }

        @Test
        fun logSet_assignsIncreasingPositions() = runBlocking {
            val bench = CatalogExercise(name = "Bench Press")
            dao.insertExercises(listOf(bench))
            val sessionId = repository.startOrResume()

            repeat(3) { repository.logSet(sessionId, bench.id, reps = 8, weightKg = 60.0) }

            val result = dao.observeSessionById(sessionId).first()
            assertEquals(listOf(0, 1, 2), result!!.sets.map { it.position }.sorted())
        }

        @Test
        fun lastSetFor_returnsTheMostRecentSet() = runBlocking {
            val bench = CatalogExercise(name = "Bench Press")
            val session = WorkoutSession()
            dao.insertExercises(listOf(bench))
            dao.insertSession(session)
            dao.insertSet(
                LoggedSet(
                    sessionId = session.id,
                    exerciseId = bench.id,
                    position = 0,
                    reps = 8,
                    weightKg = 60.0,
                    createdAt = 1000L
                )
            )
            dao.insertSet(LoggedSet(sessionId = session.id, exerciseId = bench.id, position = 1, reps = 6, weightKg = 62.5, createdAt = 2000L))

            val last = repository.lastSetFor(bench.id)

            assertEquals(62.5, last!!.weightKg, 0.0)
        }

        @Test
        fun endSession_makesSessionNoLongerUnfinished() = runBlocking {
            val sessionId = repository.startOrResume()

            repository.endSession(sessionId)

            assertNull(dao.findUnfinished())
        }
    }
}