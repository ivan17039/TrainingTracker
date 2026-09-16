package com.ivanb.trainingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.ivanb.trainingtracker.Week1.ExerciseForm
import com.ivanb.trainingtracker.Week1.NavigationViewModel
import com.ivanb.trainingtracker.Week1.WorkoutForm
import com.ivanb.trainingtracker.Week1.WorkoutFormScreen
import com.ivanb.trainingtracker.Week1.WorkoutDetail
import com.ivanb.trainingtracker.Week1.WorkoutDetailScreen
import com.ivanb.trainingtracker.Week1.WorkoutList
import com.ivanb.trainingtracker.Week1.WorkoutListScreen
import com.ivanb.trainingtracker.Week2.ExerciseFormScreen
import com.ivanb.trainingtracker.ui.theme.TrainingTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrainingTrackerTheme {
                Scaffold { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TrainingTrackerApp()
                    }
                }
            }
        }
    }
}
@Composable
fun TrainingTrackerApp(navViewModel: NavigationViewModel = viewModel()) {
    val backStack = navViewModel.backStack

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<WorkoutList> {
                WorkoutListScreen(
                    onWorkoutClick = { id -> backStack.add(WorkoutDetail(id)) },
                    onAddClick = {backStack.add(WorkoutForm())}
                )
            }
            entry<WorkoutDetail> { key ->
                WorkoutDetailScreen(
                    workoutId = key.workoutId,
                    onBack = { backStack.removeLastOrNull() },
                    onEditClick = { id -> backStack.add(WorkoutForm(id)) },
                    onAddExerciseClick = {id -> backStack.add(ExerciseForm(workoutId = id))}
                )
            }
            entry<ExerciseForm> { key ->
                ExerciseFormScreen(
                    workoutId = key.workoutId,
                    onBack = { backStack.removeLastOrNull() },
                    onSaved = { backStack.removeLastOrNull() }
                )
            }

            entry<WorkoutForm> { key ->
                WorkoutFormScreen(
                    workoutId = key.workoutId,
                    onBack = { backStack.removeLastOrNull() },
                    onSaved = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrainingTrackerTheme {
        TrainingTrackerApp()
    }
}