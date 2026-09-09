package com.ivanb.trainingtracker.Week1

import javax.inject.Inject

// @Inject constructor - Hilt može napravit ovu klasu kad god nekome treba
class WorkoutRepository @Inject constructor() {

    fun getAllWorkouts(): List<Workout> {
        return DummyData.workouts
    }

    fun getWorkoutById(id: Int): Workout? {
        return DummyData.workouts.find { it.id == id }
    }
}