package edu.ucne.finanzen.presentation.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.model.TransactionType
import edu.ucne.finanzen.ui.theme.FinanzenTheme

@Composable
fun TransactionListScreen(
    transactions: List<Transaction>,
    onDeleteTransaction: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(transactions, key = { it.transactionId }) { transaction ->
            TransactionListItem(
                transaction = transaction,
                onDelete = { onDeleteTransaction(transaction.transactionId) }
            )
        }
    }
}

@Composable
fun TransactionListItem(
    transaction: Transaction,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    transaction.category.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                if (transaction.description.isNotBlank()) {
                    Text(
                        transaction.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    transaction.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${if (transaction.type == TransactionType.INCOME) "+" else "-"}$${"%.2f".format(transaction.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.type == TransactionType.INCOME) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.error
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionListScreenPreview() {
    FinanzenTheme() {
        TransactionListScreen(
            transactions = listOf(
                Transaction(
                    transactionId = 1,
                    type = TransactionType.INCOME,
                    amount = 1000.0,
                    category = edu.ucne.finanzen.domain.model.CategoryType.SALARIO ,
                    description = "Salario mensual",
                    date = "01/12/2023"
                ),
                Transaction(
                    transactionId = 2,
                    type = TransactionType.EXPENSE,
                    amount = 50.0,
                    category = edu.ucne.finanzen.domain.model.CategoryType.ALIMENTACION ,
                    description = "Supermercado",
                    date = "02/12/2023"
                )
            ),
            onDeleteTransaction = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionListItemPreview() {
    FinanzenTheme() {
        TransactionListItem(
            transaction = Transaction(
                transactionId = 1,
                type = TransactionType.INCOME,
                amount = 1000.0,
                category = edu.ucne.finanzen.domain.model.CategoryType.SALARIO ,
                description = "Salario mensual",
                date = "01/12/2023"
            ),
            onDelete = {}
        )
    }
}