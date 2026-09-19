package com.ivanb.trainingtracker.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
    val backStack = mutableStateListOf<Any>(WorkoutList)
}