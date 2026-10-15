package com.ivanb.trainingtracker.data

import androidx.room.Embedded
import androidx.room.Relation

data class SessionWithSets(
    @Embedded val session: WorkoutSession,
    @Relation(parentColumn = "id", entityColumn = "sessionId")
    val sets: List<LoggedSet>
)