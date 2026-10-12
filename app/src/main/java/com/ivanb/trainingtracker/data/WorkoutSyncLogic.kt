package com.ivanb.trainingtracker.data

fun findNewWorkouts(
    remote: List<RemoteWorkout>,
    local: List<Workout>
): List<RemoteWorkout> {
    val knownRemoteIds = local
        .mapNotNull { it.remoteId }
        .toSet()

    return remote.filter { remoteWorkout ->
        remoteWorkout.id !in knownRemoteIds
    }
}