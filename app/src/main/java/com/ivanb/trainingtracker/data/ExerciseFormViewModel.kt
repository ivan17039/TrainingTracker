package com.ivanb.trainingtracker.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class ExerciseFormViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel(){

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _sets = MutableStateFlow("")
    val sets: StateFlow<String> = _sets.asStateFlow()

    private val _reps = MutableStateFlow("")
    val reps: StateFlow<String> = _reps.asStateFlow()

    private val _weight = MutableStateFlow("")
    val weight: StateFlow<String> = _weight.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var editingExercise: Exercise? = null


    fun onNameChange(value: String){
        _name.value = value
        clearError()
    }

    fun onSetsChange(value: String){
        _sets.value = value
        clearError()
    }

    fun onRepsChange(value: String){
        _reps.value = value
        clearError()
    }

    fun onWeightChange(value: String){
        _weight.value = value
        clearError()
    }

    fun clearError(){
        if(_error.value != null){
            _error.value = null
        }
    }

    fun clearForm() {
        _name.value = ""
        _sets.value = ""
        _reps.value = ""
        _weight.value = ""
        _error.value = null
        editingExercise = null
    }

    fun onSaveClick(workoutId: Int, onSaved: () -> Unit) {
        val trimmedName = _name.value.trim()
        if (trimmedName.isBlank()) {
            _error.value = "Ime vježbe je obavezno"
            return
        }

        val setsValue = _sets.value.toIntOrNull()
        if (setsValue == null || setsValue <= 0) {
            _error.value = "Serije moraju biti broj veći od 0"
            return
        }

        val repsValue = _reps.value.toIntOrNull()
        if (repsValue == null || repsValue <= 0) {
            _error.value = "Ponavljanja moraju biti broj veći od 0"
            return
        }

        val weightValue = _weight.value.toDoubleOrNull()
        if (weightValue == null || weightValue < 0) {
            _error.value = "Kilaža mora biti broj (0 ili više)"
            return
        }

        viewModelScope.launch {
            val existing = editingExercise
            if(existing != null){
                repository.updateExerciseInWorkout(
                    workoutId,
                    existing.copy(
                        name = trimmedName,
                        sets = setsValue,
                        reps = repsValue,
                        weightKg = weightValue
                    )
                )
            }
            else{
            repository.addExerciseToWorkout(
                workoutId,
                Exercise(
                    name = trimmedName,
                    sets = setsValue,
                    reps = repsValue,
                    weightKg = weightValue
                )
            )
            }
            clearForm()
            onSaved()
        }
    }
    fun loadForEdit(workoutId: Int, exerciseId: String) {
        viewModelScope.launch {
            val workout = repository.getWorkoutById(workoutId)
            val exercise = workout?.exercises?.find { it.id == exerciseId }
            if (exercise != null) {
                editingExercise = exercise
                _name.value = exercise.name
                _sets.value = exercise.sets.toString()
                _reps.value = exercise.reps.toString()
                _weight.value = exercise.weightKg.toString()
            }
        }
    }


}