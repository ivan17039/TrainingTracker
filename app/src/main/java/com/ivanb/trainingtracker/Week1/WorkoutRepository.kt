package com.ivanb.trainingtracker.Week1

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao
) {
    val workouts: Flow<List<Workout>> = workoutDao.getAll()

    suspend fun getWorkoutById(id: Int): Workout? {
        return workoutDao.getById(id)
    }

    suspend fun addWorkout(workout: Workout) {
        workoutDao.insert(workout)
    }

    suspend fun seedIfEmpty() {
        if (workoutDao.count() == 0) {
            workoutDao.insertAll(DummyData.workouts)
        }
    }

    suspend fun deleteWorkout(id: Int){
        workoutDao.deleteById(id)
    }
}