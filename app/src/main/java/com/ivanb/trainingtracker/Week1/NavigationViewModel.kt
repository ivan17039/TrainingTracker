package com.ivanb.trainingtracker.Week1

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NavigationViewModel : ViewModel() {
    val backStack = mutableStateListOf<Any>(WorkoutList)
}