package com.ivanb.trainingtracker.data

import com.google.gson.annotations.SerializedName

data class RegisterRequest(val email: String, val password: String)
data class RegisterResponse(val userId: Int)
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)

data class RemoteWorkout(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    val name: String,
    @SerializedName("date_millis") val dateMillis: Long,
    val exercises: List<Exercise>
)

data class CreateWorkoutRequest(
    val name: String,
    val dateMillis: Long,
    val exercises: List<Exercise>
)

fun RemoteWorkout.toWorkout(): Workout {
    return Workout(
        name = name,
        dateMillis = dateMillis,
        exercises = exercises,
        remoteId = id
    )
}