package com.ivanb.trainingtracker.data

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WorkoutApi {
    @GET("api/workouts")
    suspend fun getWorkouts(): List<RemoteWorkout>

    @POST("api/workouts")
    suspend fun createWorkout(@Body request: CreateWorkoutRequest): RemoteWorkout

    @PUT("api/workouts/{id}")
    suspend fun updateWorkout(@Path("id") id: Int, @Body request: CreateWorkoutRequest): RemoteWorkout
}