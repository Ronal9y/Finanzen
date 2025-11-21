package edu.ucne.finanzen.presentation.goals

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.finanzen.R
import edu.ucne.finanzen.common.components.LottieAnimationView
import edu.ucne.finanzen.ui.theme.FinanzenTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    onBack: () -> Unit,
    viewModel: GoalsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val justCompletedGoal = state.goals.find { it.currentAmount >= it.targetAmount }
    var showCelebration by remember { mutableStateOf(false) }

    LaunchedEffect(justCompletedGoal?.goalId) {
        justCompletedGoal?.let {
            showCelebration = true
        }
    }

    if (showAddDialog) {
        AddGoalDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, target, desc, deadline ->
                viewModel.onEvent(
                    GoalsEvent.AddGoal(
                        name = name,
                        targetAmount = target,
                        description = desc,
                        deadline = deadline
                    )
                )
                showAddDialog = false
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Metas de Ahorro",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Meta")
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (state.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (state.goals.isEmpty()) {
                    EmptyGoalsSection { showAddDialog = true }
                } else {
                    GoalListScreen(
                        goals = state.goals,
                        onDeleteGoal = { viewModel.onEvent(GoalsEvent.DeleteGoal(it)) },
                        onAddToGoal = { id, amount -> viewModel.onEvent(GoalsEvent.AddToGoal(id, amount)) }
                    )
                }
            }
        }

        if (showCelebration) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimationView(
                    resId = R.raw.celebration,
                    modifier = Modifier.size(300.dp),
                    iterations = 1
                )
            }

            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(5000)
                showCelebration = false
            }
        }
    }
}
@Composable
fun EmptyGoalsSection(onAddGoal: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LottieAnimationView(
                resId = R.raw.saving_money,
                modifier = Modifier.size(250.dp),
                iterations = Int.MAX_VALUE
            )

            Text(
                "No hay metas",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "Crea tu primera meta de ahorro",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalsScreenPreview() {
    FinanzenTheme() {
        GoalsScreen(onBack = {})
    }
}