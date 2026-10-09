package com.ivanb.trainingtracker.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    fun observeWorkout(id: Int): Flow<Workout?>{
        return workoutDao.observeById(id)
    }

    suspend fun addExerciseToWorkout(workoutId: Int, exercise: Exercise){
        val workout = workoutDao.getById(workoutId) ?: return
        val updated = workout.copy(exercises = workout.exercises + exercise)
        workoutDao.insert(updated)
    }

    suspend fun updateExerciseInWorkout(workoutId: Int, exercise: Exercise){
        val workout = workoutDao.getById(workoutId) ?: return
        val updated = workout.copy(exercises = workout.exercises.map {if (it.id == exercise.id) exercise else it})
        workoutDao.insert(updated)
    }

    suspend fun deleteExerciseFromWorkout(workoutId: Int, exerciseId: String){
        val workout = workoutDao.getById(workoutId) ?: return
        val updated = workout.copy(exercises = workout.exercises.filter { it.id != exerciseId })
        workoutDao.insert(updated)
    }

    suspend fun getAllWorkoutsOnce(): List<Workout> {
        return workouts.first()
    }

    suspend fun setRemoteId(localId: Int, remoteId: Int) {
        val workout = workoutDao.getById(localId) ?: return
        workoutDao.insert(workout.copy(remoteId = remoteId))
    }

}