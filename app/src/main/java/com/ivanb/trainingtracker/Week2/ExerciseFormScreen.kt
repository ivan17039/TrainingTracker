package com.ivanb.trainingtracker.Week2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseFormScreen(
    workoutId: Int,
    exerciseId: String?,
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: ExerciseFormViewModel = viewModel()
) {
    android.util.Log.d("ExerciseForm", "workoutId = $workoutId, exerciseId = $exerciseId")
    val name by viewModel.name.collectAsState()
    val sets by viewModel.sets.collectAsState()
    val reps by viewModel.reps.collectAsState()
    val weight by viewModel.weight.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(exerciseId) {
        if(exerciseId != null){
            viewModel.loadForEdit(workoutId, exerciseId)
        } else {
            viewModel.clearForm()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (exerciseId == null) "Nova vježba" else "Uredi vježbu") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Natrag")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Ime vježbe") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = sets,
                onValueChange = viewModel::onSetsChange,
                label = { Text("Serije") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = reps,
                onValueChange = viewModel::onRepsChange,
                label = { Text("Ponavljanja") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = weight,
                onValueChange = viewModel::onWeightChange,
                label = { Text("Kilaža (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = error!!)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.onSaveClick(workoutId, onSaved) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (exerciseId == null) "Spremi vježbu" else "Spremi promjene")
            }
        }
    }
}