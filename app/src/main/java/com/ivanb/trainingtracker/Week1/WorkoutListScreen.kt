package com.ivanb.trainingtracker.Week1

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ivanb.trainingtracker.ui.theme.TrainingTrackerTheme

@Composable
fun WorkoutListScreen(workouts: List<Workout>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(workouts) { workout ->
            WorkoutCard(workout = workout)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun WorkoutListScreenPreview() {
    TrainingTrackerTheme {
        WorkoutListScreen(workouts = DummyData.workouts)
    }
}