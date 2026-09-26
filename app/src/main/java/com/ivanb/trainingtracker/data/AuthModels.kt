package com.ivanb.trainingtracker.data

data class RegisterRequest(val email: String, val password: String)
data class RegisterResponse(val userId: Int)
data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String)