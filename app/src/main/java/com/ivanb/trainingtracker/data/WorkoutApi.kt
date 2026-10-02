package com.ivanb.trainingtracker.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WorkoutApi {
    @GET("api/workouts")
    suspend fun getWorkouts(): List<RemoteWorkout>

    @POST("api/workouts")
    suspend fun createWorkout(@Body request: CreateWorkoutRequest): RemoteWorkout
}