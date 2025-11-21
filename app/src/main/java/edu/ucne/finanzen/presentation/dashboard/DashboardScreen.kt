package edu.ucne.finanzen.presentation.dashboard


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.finanzen.ui.theme.FinanzenTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onAddTransaction: () -> Unit,
    onViewTransactions: () -> Unit,
    onViewGoals: () -> Unit,
    onViewDebts: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(DashboardEvent.Refresh)
    }

    Scaffold(
        topBar = { /* igual */ },
        floatingActionButton = { /* igual */ }
    ) { innerPadding ->
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            DashboardContent(
                state = state,
                onViewTransactions = onViewTransactions,
                onViewGoals = onViewGoals,
                onViewDebts = onViewDebts,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}