package com.ivanb.trainingtracker.Week1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutCreateViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()

    fun onNameChange(newName: String) {
        _name.value = newName
        if (_nameError.value != null) {
            _nameError.value = null
        }
    }

    fun onSaveClick(onSaved: () -> Unit) {
        val trimmed = _name.value.trim()
        if (trimmed.isBlank()) {
            _nameError.value = "Ime treninga je obavezno"
            return
        }
        viewModelScope.launch {
            repository.addWorkout(
                Workout(
                    name = trimmed,
                    dateMillis = System.currentTimeMillis()
                )
            )
            onSaved()
        }
    }
}