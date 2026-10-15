package com.ivanb.trainingtracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Workout::class, CatalogExercise::class, WorkoutSession::class, LoggedSet::class], version = 4)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao

    abstract  fun sessionDao(): SessionDao
}