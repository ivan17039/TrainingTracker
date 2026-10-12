package com.ivanb.trainingtracker

import com.ivanb.trainingtracker.data.Exercise
import com.ivanb.trainingtracker.data.RemoteWorkout
import com.ivanb.trainingtracker.data.Workout
import com.ivanb.trainingtracker.data.toWorkout
import junit.framework.TestCase.assertEquals
import org.junit.Test

class WorkoutMappersTest {

    @Test
    fun toWorkout_setsRemoteIdFromRemoteId_andLeavesLocalIdAtZero() {
        // Arrange
        val remote = RemoteWorkout(
            id = 47,
            userId = 5,
            name = "Push Day",
            dateMillis = 1_700_000_000_000L,
            exercises = emptyList()
        )

        // Act
        val local = remote.toWorkout()

        // Assert
        assertEquals(47, local.remoteId)
        assertEquals(0, local.id)
        assertEquals("Push Day", local.name)
        assertEquals(1_700_000_000_000L, local.dateMillis)
    }

    @Test
    fun toWorkout_keepsExercises() {
        val exercise = Exercise(name = "Bench Press", sets = 3, reps = 8, weightKg = 60.0)
        val remote = RemoteWorkout(
            id = 1,
            userId = 5,
            name = "Push",
            dateMillis = 0L,
            exercises = listOf(exercise)
        )

        val local = remote.toWorkout()

        assertEquals(1, local.exercises.size)
        assertEquals("Bench Press", local.exercises[0].name)
    }

    @Test
    fun workout_hasDefaultValues() {
        val workout = Workout(
            name = "Leg Day",
            dateMillis = 1_700_000_000_000L
        )

        assertEquals(0, workout.id)
        assertEquals(null, workout.remoteId)
        assertEquals(0, workout.exercises.size)
    }
}