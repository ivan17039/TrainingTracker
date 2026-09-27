package com.ivanb.trainingtracker.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage
){
    suspend fun login(email: String, password: String) {
        val response = authApi.login(LoginRequest(email, password))
        tokenStorage.saveToken(response.token)
    }

    suspend fun register(email: String, password: String): Int {
        return authApi.register(RegisterRequest(email, password)).userId
    }

    fun logout() {
        tokenStorage.clearToken()
    }
}