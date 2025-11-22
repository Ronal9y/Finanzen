package edu.ucne.finanzen.presentation.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.finanzen.domain.model.Goal
import edu.ucne.finanzen.ui.theme.FinanzenTheme

@Composable
fun GoalListScreen(
    goals: List<Goal>,
    onDeleteGoal: (Int) -> Unit,
    onAddToGoal: (Int, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(goals, key = { it.goalId }) { goal ->
            GoalListItem(
                goal = goal,
                onDelete = { onDeleteGoal(goal.goalId) },
                onAddToGoal = { amount -> onAddToGoal(goal.goalId, amount) }
            )
        }
    }
}

@Composable
fun GoalListItem(
    goal: Goal,
    onDelete: () -> Unit,
    onAddToGoal: (Double) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var addAmount by remember { mutableStateOf("") }
    var showConfirm by remember { mutableStateOf(false) }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("¿Eliminar meta?") },
            text = {
                Text("Esta acción no se puede deshacer. ¿Estás seguro de eliminar esta meta?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showConfirm = false
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        goal.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (goal.description.isNotBlank()) {
                        Text(
                            goal.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        "Meta: ${goal.deadline}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { showConfirm = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val progress = (goal.currentAmount / goal.targetAmount).coerceIn(0.0, 1.0)
            val percentage = (progress * 100).toInt()

            Text(
                "${"%.2f".format(goal.currentAmount)} $ de ${"%.2f".format(goal.targetAmount)} $",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "$percentage% completado",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Agregar Ahorro")
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Agregar Ahorro") },
            text = {
                Column {
                    Text("Monto a agregar:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = addAmount,
                        onValueChange = { addAmount = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("0.00") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = addAmount.toDoubleOrNull() ?: 0.0
                        if (amount > 0) {
                            onAddToGoal(amount)
                            showAddDialog = false
                            addAmount = ""
                        }
                    }
                ) {
                    Text("Agregar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoalListScreenPreview() {
    FinanzenTheme {
        GoalListScreen(
            goals = listOf(
                Goal(
                    goalId = 1,
                    name = "Casa nueva",
                    targetAmount = 500000.0,
                    currentAmount = 50000.0,
                    deadline = "07 oct 2026",
                    usuarioId = 1,
                    description = "mudarme"
                ),
                Goal(
                    goalId = 2,
                    name = "Fondo de emergencia",
                    targetAmount = 10000.0,
                    currentAmount = 2500.0,
                    deadline = "31 dic 2024",
                    usuarioId = 1,
                    description = "Ahorro para emergencias"
                )
            ),
            onDeleteGoal = {},
            onAddToGoal = { _, _ -> }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GoalListItemPreview() {
    FinanzenTheme {
        GoalListItem(
            goal = Goal(
                goalId = 1,
                name = "Casa nueva",
                targetAmount = 500000.0,
                currentAmount = 50000.0,
                deadline = "07 oct 2026",
                usuarioId = 1,
                description = "mudarme"
            ),
            onDelete = {},
            onAddToGoal = {}
        )
    }
}
