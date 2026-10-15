package com.ivanb.trainingtracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao{

    @Insert
    suspend fun insertExercise(exercise: CatalogExercise)

    @Insert
    suspend fun insertSession(session: WorkoutSession)

    @Insert
    suspend fun insertSet(set: LoggedSet)

    @Transaction
    @Query("SELECT * FROM workout_sessions WHERE deletedAt IS NULL ORDER BY startedAt DESC")
    fun observeSessions(): Flow<List<SessionWithSets>>

    @Query("SELECT * FROM workout_sessions WHERE endedAt IS NULL AND deletedAt IS NULL ORDER BY startedAt DESC LIMIT 1")
    suspend fun findUnfinished(): WorkoutSession?
}

