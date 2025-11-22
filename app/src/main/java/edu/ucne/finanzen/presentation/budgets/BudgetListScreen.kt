package edu.ucne.finanzen.presentation.budgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.finanzen.domain.model.Budget
import edu.ucne.finanzen.domain.model.CategoryType
import edu.ucne.finanzen.ui.theme.FinanzenTheme

@Composable
fun BudgetListScreen(
    budgets: List<Budget>,
    onDeleteBudget: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(budgets, key = { it.budgetId }) { budget ->
            BudgetListItem(
                budget = budget,
                onDelete = { onDeleteBudget(budget.budgetId) }
            )
        }
    }
}

@Composable
fun BudgetListItem(
    budget: Budget,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    budget.category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val progress = (budget.spent / budget.limit).coerceIn(0.0, 1.0)
            val percentage = (progress * 100).toInt()

            Text(
                "${"%.2f".format(budget.spent)} $ / ${"%.2f".format(budget.limit)} $",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = progress.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = when {
                    percentage >= budget.alertThreshold -> MaterialTheme.colorScheme.error
                    percentage >= 80 -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.primary
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "$percentage% utilizado",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Restante: ${"%.2f".format(budget.limit - budget.spent)} $",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetListScreenPreview() {
    FinanzenTheme {
        BudgetListScreen(
            budgets = listOf(
                Budget(
                    budgetId = 1,
                    category = CategoryType.ALIMENTACION,
                    limit = 600.0,
                    spent = 450.0,
                    month = "2025-11",
                    alertThreshold = 80,
                    usuarioId = 1
                ),
                Budget(
                    budgetId = 2,
                    category = CategoryType.TRANSPORTE,
                    limit = 300.0,
                    spent = 310.0,
                    month = "2025-11",
                    alertThreshold = 80,
                    usuarioId = 1
                )
            ),
            onDeleteBudget = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetListItemPreview() {
    FinanzenTheme {
        BudgetListItem(
            budget = Budget(
                budgetId = 1,
                category = CategoryType.ENTRETENIMIENTO,
                limit = 200.0,
                spent = 180.0,
                month = "2025-11",
                alertThreshold = 80,
                usuarioId = 1
            ),
            onDelete = {}
        )
    }
}
