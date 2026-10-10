package com.ivanb.trainingtracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivanb.trainingtracker.data.AccountViewModel
import com.ivanb.trainingtracker.data.WorkoutCard
import com.ivanb.trainingtracker.data.WorkoutListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutListScreen(
    onWorkoutClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: WorkoutListViewModel = viewModel(),
    accountViewModel: AccountViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val workouts by viewModel.workouts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val resultMessage by accountViewModel.resultMessage.collectAsState()

    val isLoggedIn by accountViewModel.isLoggedIn.collectAsState()
    val logoutFailed by accountViewModel.logoutFailed.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

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
        topBar = {
            TopAppBar(
                title = {
                    Text("Treninzi")
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (isLoggedIn) {
                                showLogoutDialog = true
                            } else {
                                onLoginClick()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isLoggedIn) {
                                Icons.Default.AccountCircle
                            } else {
                                Icons.Default.Person
                            },
                            contentDescription = if (isLoggedIn) {
                                "Račun (odjava)"
                            } else {
                                "Prijava"
                            }
                        )
                    }

                    IconButton(
                        onClick = accountViewModel::onSyncClick,
                        enabled = isLoggedIn
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "Sinkroniziraj"
                        )
                    }

                    IconButton(
                        onClick = accountViewModel::onRestoreClick,
                        enabled = isLoggedIn
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "Vrati iz oblaka"
                        )
                    }
                }
            )
        },
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

    if (resultMessage != null) {
        AlertDialog(
            onDismissRequest = accountViewModel::clearResult,
            title = {
                Text("Sinkronizacija")
            },
            text = {
                Text(resultMessage!!)
            },
            confirmButton = {
                TextButton(
                    onClick = accountViewModel::clearResult
                ) {
                    Text("U redu")
                }
            }
        )
    }
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text("Odjava")
            },
            text = {
                Text(
                    "Treninzi se prvo spremaju u oblak, pa se brišu s ovog uređaja. " +
                            "Ako spremanje ne uspije, odjava se prekida."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        accountViewModel.onLogoutClick()
                    }
                ) {
                    Text("Odjavi me")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text("Odustani")
                }
            }
        )
    }

    if (logoutFailed) {
        AlertDialog(
            onDismissRequest = accountViewModel::dismissLogoutFailed,
            title = {
                Text("Odjava nije uspjela")
            },
            text = {
                Text(
                    "Treninge nije bilo moguće spremiti u oblak (provjeri vezu i prijavu). " +
                            "Ništa nije izgubljeno. Ako se ipak odjaviš, treninzi koji nisu " +
                            "spremljeni bit će obrisani s ovog uređaja."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = accountViewModel::onForceLogoutClick
                ) {
                    Text("Odjavi se svejedno")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = accountViewModel::dismissLogoutFailed
                ) {
                    Text("Odustani")
                }
            }
        )
    }
}