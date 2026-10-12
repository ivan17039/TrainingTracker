package com.ivanb.trainingtracker.data

import org.junit.Assert.assertEquals
import org.junit.Test

class WorkoutSyncLogicTest {

    @Test
    fun findNewWorkouts_returnsOnlyRemoteWorkoutsMissingLocally() {
        val local = listOf(
            Workout(
                id = 1,
                remoteId = 10,
                name = "Push",
                dateMillis = 0L
            ),
            Workout(
                id = 2,
                remoteId = 11,
                name = "Pull",
                dateMillis = 0L
            )
        )

        val remote = listOf(
            RemoteWorkout(
                id = 10,
                userId = 5,
                name = "Push",
                dateMillis = 0L,
                exercises = emptyList()
            ),
            RemoteWorkout(
                id = 11,
                userId = 5,
                name = "Pull",
                dateMillis = 0L,
                exercises = emptyList()
            ),
            RemoteWorkout(
                id = 12,
                userId = 5,
                name = "Legs",
                dateMillis = 0L,
                exercises = emptyList()
            )
        )

        val result = findNewWorkouts(remote, local)

        assertEquals(1, result.size)
        assertEquals(12, result[0].id)
        assertEquals("Legs", result[0].name)
    }

    @Test
    fun findNewWorkouts_returnsAll_whenLocalListIsEmpty() {
        val remote = listOf(
            RemoteWorkout(
                id = 10,
                userId = 5,
                name = "Push",
                dateMillis = 0L,
                exercises = emptyList()
            ),
            RemoteWorkout(
                id = 11,
                userId = 5,
                name = "Pull",
                dateMillis = 0L,
                exercises = emptyList()
            )
        )

        val result = findNewWorkouts(remote, emptyList())

        assertEquals(2, result.size)
    }

    @Test
    fun findNewWorkouts_returnsEmpty_whenEverythingAlreadyExistsLocally() {
        val local = listOf(
            Workout(
                id = 1,
                remoteId = 10,
                name = "Push",
                dateMillis = 0L
            )
        )

        val remote = listOf(
            RemoteWorkout(
                id = 10,
                userId = 5,
                name = "Push",
                dateMillis = 0L,
                exercises = emptyList()
            )
        )

        val result = findNewWorkouts(remote, local)

        assertEquals(0, result.size)
    }

    @Test
    fun findNewWorkouts_ignoresLocalWorkoutsWithoutRemoteId() {
        val local = listOf(
            Workout(
                id = 1,
                remoteId = null,
                name = "Unsynced local workout",
                dateMillis = 0L
            )
        )

        val remote = listOf(
            RemoteWorkout(
                id = 10,
                userId = 5,
                name = "Push",
                dateMillis = 0L,
                exercises = emptyList()
            )
        )

        val result = findNewWorkouts(remote, local)

        assertEquals(1, result.size)
        assertEquals(10, result[0].id)
    }
}