package com.ivanb.trainingtracker.data

data object WorkoutList

data class WorkoutDetail(val workoutId: Int)

data class WorkoutForm(val workoutId: Int ? = null)

data class ExerciseForm(val workoutId: Int, val exerciseId: String ? = null)