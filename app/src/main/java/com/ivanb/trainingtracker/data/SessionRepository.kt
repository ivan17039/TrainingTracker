package com.ivanb.trainingtracker.data

import jakarta.inject.Inject

class SessionRepository @Inject constructor(
    private val sessionDao: SessionDao
) {

    fun observeUnfinished() = sessionDao.observeUnfinished()
    fun observeSession(id: String) = sessionDao.observeSessionById(id)
    fun observeCatalog() = sessionDao.observeCatalog()

    suspend fun startOrResume(): String {
        val existing = sessionDao.findUnfinished()
        if (existing != null) return existing.id
        val session = WorkoutSession()
        sessionDao.insertSession(session)
        return session.id
    }

    suspend fun logSet(sessionId: String, exerciseId: String, reps: Int, weightKg: Double) {
        sessionDao.insertSet(
            LoggedSet(
                sessionId = sessionId,
                exerciseId = exerciseId,
                position = sessionDao.nextPosition(sessionId),
                reps = reps,
                weightKg = weightKg
            )
        )
    }

    suspend fun lastSetFor(exerciseId: String): LoggedSet? = sessionDao.lastSetFor(exerciseId)

    suspend fun endSession(sessionId: String) {
        val session = sessionDao.findSessionById(sessionId) ?: return
        val now = System.currentTimeMillis()
        sessionDao.updateSession(session.copy(endedAt = now, updatedAt = now))
    }

    suspend fun seedCatalogIfEmpty() {
        if (sessionDao.catalogCount() == 0) {
            sessionDao.insertExercises(
                listOf(
                    "Bench Press", "Squat", "Deadlift", "Overhead Press",
                    "Barbell Row", "Pull-up", "Romanian Deadlift", "Lat Pulldown"
                ).map { CatalogExercise(name = it) }
            )
        }
    }
}