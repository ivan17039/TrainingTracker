package com.ivanb.trainingtracker.Week1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(workoutId: Int, onBack: () -> Unit, onEditClick: (Int) ->Unit) {
    val workout = remember(workoutId) { DummyData.workouts.find { it.id == workoutId } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(workout?.name ?: "Trening") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Natrag")
                    }
                }
            )
        }
    ) { padding ->
        if (workout == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Trening nije pronađen")
            }
        } else {
            // workout nije null → sad gledamo ima li vježbi
            if (workout.exercises.isEmpty()) {
                // NEMA vježbi → prikaži samo poruku, BEZ LazyColumn
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nema unesenih vježbi.")
                }
            } else {
                // IMA vježbi → prikaži LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    items(workout.exercises) { exercise ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = exercise.name,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "${exercise.sets} x ${exercise.reps} @ ${exercise.weightKg}kg",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Button(
                                onClick = { onEditClick(workoutId) }
                            ) {
                                Text("Uredi")
                            }
                        }
                    }
                }
            }
        }
    }
}