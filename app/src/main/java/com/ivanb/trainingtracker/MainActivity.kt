package com.ivanb.trainingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ivanb.trainingtracker.Week1.DummyData
import com.ivanb.trainingtracker.Week1.WorkoutListScreen
import com.ivanb.trainingtracker.ui.theme.TrainingTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainingTrackerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WorkoutListScreen(workouts = DummyData.workouts)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrainingTrackerTheme {
        WorkoutListScreen(workouts = DummyData.workouts)
    }
}