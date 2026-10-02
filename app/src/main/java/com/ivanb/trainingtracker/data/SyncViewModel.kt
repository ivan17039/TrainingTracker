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
    private val syncRepository: SyncRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _resultMessage = MutableStateFlow<String?>(null)
    val resultMessage: StateFlow<String?> = _resultMessage.asStateFlow()

    fun onSyncClick() {
        viewModelScope.launch {
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

    fun clearResult() {
        _resultMessage.value = null
    }
}