package com.ivanb.trainingtracker.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage,
    private val syncRepository: SyncRepository,
    private val workoutRepository: WorkoutRepository
){
    private val _isLoggedIn = MutableStateFlow(tokenStorage.getToken() != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    suspend fun login(email: String, password: String) {
        val response = authApi.login(LoginRequest(email, password))
        tokenStorage.saveToken(response.token)
        _isLoggedIn.value = true
    }

    suspend fun register(email: String, password: String): Int {
        return authApi.register(RegisterRequest(email, password)).userId
    }

    suspend fun logout() {
        syncRepository.pushAllWorkouts()
        discardLocalSession()
    }

    suspend fun discardLocalSession() {
        workoutRepository.clearAll()
        tokenStorage.clearToken()
        _isLoggedIn.value = false
    }

}