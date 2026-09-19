package com.ivanb.trainingtracker.data

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WorkoutCard(workout: Workout, modifier: Modifier = Modifier){
    val dateFormat = remember { SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()) }
    val dateString = dateFormat.format(Date(workout.dateMillis))
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray).padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically){
                Text(text = workout.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = "Trening ikona"
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = dateString, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${workout.exercises.size} vježbi", style = MaterialTheme.typography.bodyMedium)
        }

    }

}