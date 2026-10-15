package com.ivanb.trainingtracker

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ivanb.trainingtracker.data.AppDatabase
import com.ivanb.trainingtracker.data.CatalogExercise
import com.ivanb.trainingtracker.data.LoggedSet
import com.ivanb.trainingtracker.data.SessionDao
import com.ivanb.trainingtracker.data.WorkoutSession
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SessionDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: SessionDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        dao = db.sessionDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun session_is_read_back_with_its_sets_in_one_piece() = runBlocking {
        val bench = CatalogExercise(name = "Bench Press")
        val session = WorkoutSession()
        dao.insertExercise(bench)
        dao.insertSession(session)
        repeat(3) { i ->
            dao.insertSet(
                LoggedSet(
                    sessionId = session.id,
                    exerciseId = bench.id,
                    position = i,
                    reps = 8,
                    weightKg = 60.0
                )
            )
        }

        val result = dao.observeSessions().first()

        assertEquals(1, result.size)
        assertEquals(3, result[0].sets.size)
    }

    @Test
    fun findUnfinished_returns_only_session_without_endedAt() = runBlocking {
        val unfinished = WorkoutSession()
        val finished = WorkoutSession(
            startedAt = System.currentTimeMillis() - 60_000,
            endedAt = System.currentTimeMillis()
        )

        dao.insertSession(unfinished)
        dao.insertSession(finished)

        val result = dao.findUnfinished()

        assertEquals(unfinished.id, result?.id)
    }
}