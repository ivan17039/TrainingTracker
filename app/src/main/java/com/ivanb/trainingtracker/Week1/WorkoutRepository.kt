package com.ivanb.trainingtracker.Week1

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

// @Inject constructor - Hilt može napravit ovu klasu kad god nekome treba
@Singleton
class WorkoutRepository @Inject constructor() {

    private val _workouts = MutableStateFlow(DummyData.workouts)
    val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()
    fun getWorkoutById(id: Int): Workout? {
        return _workouts.value.find { it.id == id }
    }

    fun addWorkout(workout: Workout) {
        _workouts.value = _workouts.value + workout
    }

    fun nextId(): Int {
        return (_workouts.value.maxOfOrNull { it.id } ?: 0) + 1
    }
}