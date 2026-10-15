package com.ivanb.trainingtracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.TimeZone
import java.util.UUID
@Entity(tableName = "catalog_exercises")
data class CatalogExercise(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val isCustom: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)

@Entity(tableName = "workout_sessions")
data class WorkoutSession(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String? = null,
    val startedAt: Long = System.currentTimeMillis(),
    val endedAt: Long? = null,
    val timezoneId: String = TimeZone.getDefault().id,
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)

@Entity(
    tableName = "logged_sets",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSession::class,
            parentColumns = ["id"],
            childColumns = ["sessionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CatalogExercise::class,
            parentColumns = ["id"],
            childColumns = ["exerciseId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index("sessionId"), Index("exerciseId")]
)
data class LoggedSet(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val sessionId: String,
    val exerciseId: String,
    val position: Int,
    val reps: Int,
    val weightKg: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)

