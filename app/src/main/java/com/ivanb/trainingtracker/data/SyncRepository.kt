package com.ivanb.trainingtracker.data

import javax.inject.Inject

class SyncRepository @Inject constructor(
    private val workoutApi: WorkoutApi,
    private val workoutRepository: WorkoutRepository
) {
    suspend fun pushAllWorkouts(): Int {
        val localWorkouts = workoutRepository.getAllWorkoutsOnce()

        for (workout in localWorkouts) {
            val request = workout.toRequest()

            if (workout.remoteId == null) {
                val remote = workoutApi.createWorkout(request)
                workoutRepository.setRemoteId(workout.id, remote.id)
            } else {
                workoutApi.updateWorkout(workout.remoteId, request)
            }
        }

        return localWorkouts.size
    }

    suspend fun pullWorkouts(): Int {
        val remoteWorkouts = workoutApi.getWorkouts()
        val localWorkouts = workoutRepository.getAllWorkoutsOnce()
        val knownRemoteIds = localWorkouts.mapNotNull { it.remoteId }.toSet()

        val newWorkouts = remoteWorkouts
            .filter { it.id !in knownRemoteIds }
            .map { it.toWorkout() }

        workoutRepository.addWorkouts(newWorkouts)
        return newWorkouts.size
    }
}