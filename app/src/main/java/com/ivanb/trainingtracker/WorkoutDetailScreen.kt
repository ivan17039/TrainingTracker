package com.ivanb.trainingtracker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.TextButton
import com.ivanb.trainingtracker.data.WorkoutDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutDetailScreen(
    workoutId: Int,
    onBack: () -> Unit,
    onEditClick: (Int) ->Unit,
    onAddExerciseClick: (Int) -> Unit,
    onEditExerciseClick: (Int, String) -> Unit,
    viewModel: WorkoutDetailViewModel = viewModel()
) {
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    val workout by viewModel.workout.collectAsState()

    LaunchedEffect(workoutId) {
        viewModel.loadWorkout(workoutId)
    }

    val currentWorkout = workout

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = {
                Text("Izbriši trening?")
            },
            text = {
                Text("Jesi li siguran da želiš izbrisati ovaj trening?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteWorkout(workoutId, onBack)
                    }
                ) {
                    Text("Izbriši")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Odustani")
                }
            }
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentWorkout?.name ?: "Trening") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Natrag")
                    }
                },
                actions = {
                    IconButton(onClick = { onEditClick(workoutId) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Uredi")
                    }
                    IconButton(
                        onClick = {
                            showDeleteDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Izbriši"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddExerciseClick(workoutId) }) {
                Icon(Icons.Default.Add, contentDescription = "Dodaj vježbu")
            }
        }
    ) { padding ->
        if (currentWorkout == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Trening nije pronađen")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // DATUM TRENINGA - dodaj ovo
                Text(
                    text = currentWorkout.dateMillis.toFormattedDate(),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                // workout nije null → sad gledamo ima li vježbi
                if (currentWorkout.exercises.isEmpty()) {
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
                    )
                    {
                        items(currentWorkout.exercises, key = { it.id }) { exercise ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = exercise.name,
                                        style = MaterialTheme.typography.titleSmall
                                    )

                                    Text(
                                        text = "${exercise.sets} x ${exercise.reps} @ ${exercise.weightKg}kg",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                IconButton(onClick = {
                                    onEditExerciseClick(
                                        workoutId,
                                        exercise.id
                                    )
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Uredi vježbu")
                                }
                                IconButton(onClick = {
                                    viewModel.deleteExercise(
                                        workoutId,
                                        exercise.id
                                    )
                                }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Izbriši vježbu"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}