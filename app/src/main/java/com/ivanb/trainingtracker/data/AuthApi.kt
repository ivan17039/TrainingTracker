package com.ivanb.trainingtracker.data

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}