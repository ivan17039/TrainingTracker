package com.ivanb.trainingtracker.data

import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log
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
        Log.d("Auth", "Kliknut logout - brišem token")

        tokenStorage.clearToken()

        Log.d("Auth", "Token je obrisan")
    }
}