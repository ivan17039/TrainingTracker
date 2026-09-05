package com.ivanb.trainingtracker.Week1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WorkoutListScreen(
    viewModel: WorkoutListViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val workouts by viewModel.workouts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        TextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            label = { Text("Pretraži treninge") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            trailingIcon = {
                // Prikaži gumb za brisanje samo ako ima unesenog teksta
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear, // Ikona iksa ("x")
                            contentDescription = "Očisti pretragu"
                        )
                    }
                }
            }
        )
        if(workouts.isEmpty()){
            Text(text = "Nema rezultata za \"$searchQuery\"",modifier = Modifier.padding(16.dp))

        } else{
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(workouts) { workout ->
                    WorkoutCard(workout = workout)

                }
            }
        }
    }
}