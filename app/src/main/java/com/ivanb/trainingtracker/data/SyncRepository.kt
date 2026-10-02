package com.ivanb.trainingtracker.data

import javax.inject.Inject

class SyncRepository @Inject constructor(
    private val workoutApi: WorkoutApi,
    private val workoutRepository: WorkoutRepository
) {
    suspend fun pushAllWorkouts(): Int {
        val localWorkouts = workoutRepository.getAllWorkoutsOnce()
        for (workout in localWorkouts) {
            android.util.Log.d("Sync", "Šaljem: ${workout.name}")
            workoutApi.createWorkout(
                CreateWorkoutRequest(
                    name = workout.name,
                    dateMillis = workout.dateMillis,
                    exercises = workout.exercises
                )
            )
        }
        return localWorkouts.size
    }
}