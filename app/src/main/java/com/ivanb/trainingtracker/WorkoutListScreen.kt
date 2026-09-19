package com.ivanb.trainingtracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivanb.trainingtracker.data.WorkoutCard
import com.ivanb.trainingtracker.data.WorkoutListViewModel

@Composable
fun WorkoutListScreen(
    onWorkoutClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    viewModel: WorkoutListViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val workouts by viewModel.workouts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val filteredWorkouts = remember(workouts, searchQuery) {
        if (searchQuery.isBlank()) {
            workouts
        } else {
            workouts.filter { workout ->
                workout.name.contains(
                    other = searchQuery,
                    ignoreCase = true
                )
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Dodaj trening"
                )
            }
        }
    ) { padding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                label = {
                    Text("Pretraži treninge")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.onSearchQueryChange("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Očisti pretragu"
                            )
                        }
                    }
                }
            )

            if (filteredWorkouts.isEmpty()) {
                Text(
                    text = if (searchQuery.isBlank()) {
                        "Nema treninga"
                    } else {
                        "Nema rezultata za \"$searchQuery\""
                    },
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = filteredWorkouts,
                        key = { workout -> workout.id }
                    ) { workout ->
                        WorkoutCard(
                            workout = workout,
                            modifier = Modifier.clickable {
                                onWorkoutClick(workout.id)
                            }
                        )
                    }
                }
            }
        }
    }
}