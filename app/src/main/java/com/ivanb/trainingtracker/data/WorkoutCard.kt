package com.ivanb.trainingtracker.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ivanb.trainingtracker.toFormattedDate

@Composable
fun WorkoutCard(workout: Workout, modifier: Modifier = Modifier){
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
            Text(text = workout.dateMillis.toFormattedDate(), style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${workout.exercises.size} vježbi", style = MaterialTheme.typography.bodyMedium)
        }

    }

}