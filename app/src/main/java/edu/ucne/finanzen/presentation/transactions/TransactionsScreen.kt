package edu.ucne.finanzen.presentation.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import edu.ucne.finanzen.ui.theme.FinanzenTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onBack: () -> Unit,
    onAddTransaction: () -> Unit,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var currentUserId by remember { mutableStateOf<Int?>(null) }

    // Obtener el currentUserId del ViewModel
    LaunchedEffect(key1 = state.currentUserId) {
        state.currentUserId?.let { userId ->
            currentUserId = userId
        }
    }

    if (showAddDialog && currentUserId != null) {
        AddTransactionDialog(
            currentUserId = currentUserId!!, // Pasamos el userId al diálogo
            onDismiss = { showAddDialog = false },
            onAdd = { transaction ->
                viewModel.onEvent(TransactionsEvent.AddTransaction(transaction))
                showAddDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Transacciones",
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
                onClick = {
                    if (currentUserId != null) {
                        showAddDialog = true
                    } else {

                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Transacción")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            FilterSection(
                selectedFilter = state.selectedFilter,
                onFilterChanged = { filter ->
                    viewModel.onEvent(TransactionsEvent.FilterChanged(filter))
                }
            )

            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.transactions.isEmpty()) {
                EmptyTransactionsSection {
                    if (currentUserId != null) {
                        showAddDialog = true
                    }
                }
            } else {
                TransactionListScreen(
                    transactions = state.transactions,
                    onDeleteTransaction = { id ->
                        viewModel.onEvent(TransactionsEvent.DeleteTransaction(id))
                    }
                )
            }
        }
    }
}

@Composable
fun FilterSection(
    selectedFilter: String,
    onFilterChanged: (String) -> Unit
) {
    val filters = listOf("Todas", "Ingresos", "Gastos")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        filters.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterChanged(filter) },
                label = { Text(filter) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun EmptyTransactionsSection(onAddTransaction: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "No hay transacciones",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                "Agrega tu primera transacción",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(onClick = onAddTransaction) {
                Text("Agregar Transacción")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionsScreenPreview() {
    FinanzenTheme {
        TransactionsScreen(
            onBack = {},
            onAddTransaction = {}
        )
    }
}