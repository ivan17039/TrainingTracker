package com.ivanb.trainingtracker.Week1

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WorkoutListViewModel : ViewModel() {

    private val allWorkouts = DummyData.workouts

    private val _workouts = MutableStateFlow(allWorkouts)
    val workouts: StateFlow<List<Workout>> = _workouts.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        _workouts.value = if (query.isBlank()) {
            allWorkouts
        } else {
            allWorkouts.filter { it.name.contains(query, ignoreCase = true) }
        }
    }
}