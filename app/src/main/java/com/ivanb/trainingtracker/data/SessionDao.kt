package com.ivanb.trainingtracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
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

    @Query("SELECT * FROM workout_sessions WHERE endedAt IS NULL AND deletedAt IS NULL ORDER BY startedAt DESC LIMIT 1")
    fun observeUnfinished(): Flow<WorkoutSession?>

    @Transaction
    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    fun observeSessionById(id: String): Flow<SessionWithSets?>

    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    suspend fun findSessionById(id: String): WorkoutSession?

    @Query("SELECT COALESCE(MAX(position) + 1, 0) FROM logged_sets WHERE sessionId = :sessionId")
    suspend fun nextPosition(sessionId: String): Int

    @Query("SELECT * FROM logged_sets WHERE exerciseId = :exerciseId AND deletedAt IS NULL ORDER BY createdAt DESC, position DESC LIMIT 1")
    suspend fun lastSetFor(exerciseId: String): LoggedSet?

    @Query("SELECT * FROM catalog_exercises WHERE deletedAt IS NULL ORDER BY name")
    fun observeCatalog(): Flow<List<CatalogExercise>>

    @Query("SELECT COUNT(*) FROM catalog_exercises")
    suspend fun catalogCount(): Int

    @Insert
    suspend fun insertExercises(exercises: List<CatalogExercise>)

    @Update
    suspend fun updateSession(session: WorkoutSession)

}

