package com.ivanb.trainingtracker.data

import javax.inject.Inject

class SyncRepository @Inject constructor(
    private val workoutApi: WorkoutApi,
    private val workoutRepository: WorkoutRepository
) {
    suspend fun pushAllWorkouts(): Int {
        val localWorkouts = workoutRepository.getAllWorkoutsOnce()

        for (workout in localWorkouts) {
            val request = CreateWorkoutRequest(
                name = workout.name,
                dateMillis = workout.dateMillis,
                exercises = workout.exercises
            )

            if (workout.remoteId == null) {
                val remote = workoutApi.createWorkout(request)
                workoutRepository.setRemoteId(workout.id, remote.id)
            } else {
                workoutApi.updateWorkout(workout.remoteId, request)
            }
        }

        return localWorkouts.size
    }
}