package com.ivanb.trainingtracker.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log
@HiltViewModel
class SyncViewModel @Inject constructor(
    private val syncRepository: SyncRepository,
    private val authRepository: AuthRepository,
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _resultMessage = MutableStateFlow<String?>(null)
    val resultMessage: StateFlow<String?> = _resultMessage.asStateFlow()

    val isLoggedIn: StateFlow<Boolean> = authRepository.isLoggedIn

    private val _logoutFailed = MutableStateFlow(false)
    val logoutFailed: StateFlow<Boolean> = _logoutFailed.asStateFlow()

    fun onLogoutClick(){
        viewModelScope.launch {
            _isLoading.value = true
            try{
                authRepository.logout()
            } catch(e: Exception){
              _logoutFailed.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onForceLogoutClick() {
        viewModelScope.launch {
            authRepository.discardLocalSession()
            _logoutFailed.value = false
        }
    }

    fun dismissLogoutFailed() {
        _logoutFailed.value = false
    }

    fun onSyncClick() {
        viewModelScope.launch {
            val token = tokenStorage.getToken()

            if (token == null) {
                _resultMessage.value = "Nisi prijavljen. Prvo se prijavi."
                return@launch
            }
            _isLoading.value = true
            try {
                val broj = syncRepository.pushAllWorkouts()
                _resultMessage.value = "Poslano $broj treninga."
            } catch (e: Exception) {
                Log.e("Sync", "Slanje treninga nije uspjelo", e)
                _resultMessage.value = "Slanje nije uspjelo. Provjeri vezu i prijavu."
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun onRestoreClick() {
        viewModelScope.launch {
            val token = tokenStorage.getToken()

            if (token == null) {
                _resultMessage.value = "Nisi prijavljen. Prvo se prijavi."
                return@launch
            }
            _isLoading.value = true
            try {
                val broj = syncRepository.pullWorkouts()
                _resultMessage.value = if (broj == 0) {
                    "Nema novih treninga u oblaku."
                } else {
                    "Preuzeto $broj treninga."
                }
            } catch (e: Exception) {
                _resultMessage.value = "Preuzimanje nije uspjelo. Provjeri vezu i prijavu."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearResult() {
        _resultMessage.value = null
    }
}