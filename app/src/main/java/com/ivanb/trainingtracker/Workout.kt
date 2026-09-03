package com.ivanb.trainingtracker

data class Workout (
    val id: Int,
    val name: String,
    val date: String,
    val exercises: List<Exercise>
)

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: Int,
    val weightKg: Double
)