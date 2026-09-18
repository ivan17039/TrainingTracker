package com.ivanb.trainingtracker.Week1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "workouts")
data class Workout (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dateMillis: Long = System.currentTimeMillis(),
    val exercises: List<Exercise> = emptyList(),
)

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: Int,
    val weightKg: Double,
    val id: String = UUID.randomUUID().toString()
)