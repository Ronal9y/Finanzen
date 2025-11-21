package edu.ucne.finanzen.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.model.TransactionType

@Composable
fun DashboardContent(
    state: DashboardState,
    onViewTransactions: () -> Unit,
    onViewGoals: () -> Unit,
    onViewDebts: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            GradientBalanceCard(
                balance = state.balance,
                totalIncome = state.totalIncome,
                totalExpenses = state.totalExpenses
            )
        }

        item {
            MetasDebtsRow(
                goalsCount = state.goalsCount,
                completionPercentage = state.completionPercentage,
                onViewGoals = onViewGoals,
                debtsCount = state.debtsCount,
                totalRemainingDebt = state.totalRemainingDebt,
                onViewDebts = onViewDebts
            )
        }

        item {
            RecentTransactionsCard(
                transactions = state.recentTransactions,
                onViewAll = onViewTransactions
            )
        }
    }
}

@Composable
fun GradientBalanceCard(
    balance: Double,
    totalIncome: Double,
    totalExpenses: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF7B1FA2), Color(0xFF9C27B0))
                    )
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                /* --- Balance Total MÁS GRANDE Y OSCURO --- */
                Text(
                    text = "Balance Total",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 1f)
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "$${"%.2f".format(balance)}",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Ingresos",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "$${"%.2f".format(totalIncome)}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Gastos",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "$${"%.2f".format(totalExpenses)}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetasDebtsRow(
    goalsCount: Int,
    completionPercentage: Double,
    onViewGoals: () -> Unit,
    debtsCount: Int,
    totalRemainingDebt: Double,
    onViewDebts: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Metas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$goalsCount", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("${"%.1f".format(completionPercentage)}% completado", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Deudas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("$${"%.2f".format(totalRemainingDebt)}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("$debtsCount activas", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun RecentTransactionsCard(
    transactions: List<Transaction>,
    onViewAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Transacciones Recientes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                TextButton(onClick = onViewAll) { Text("Ver todas") }
            }
            Spacer(Modifier.height(8.dp))
            if (transactions.isEmpty()) {
                Text("No hay transacciones recientes", style = MaterialTheme.typography.bodyMedium)
            } else {
                transactions.take(5).forEach { tx ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(tx.category.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                            if (tx.description.isNotBlank()) Text(tx.description, style = MaterialTheme.typography.bodySmall)
                            Text(tx.date, style = MaterialTheme.typography.bodySmall)
                        }
                        Text(
                            "${if (tx.type == TransactionType.INCOME) "+" else "-"}$${"%.2f".format(tx.amount)}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (tx.type == TransactionType.INCOME) Color(0xFF388E3C) else Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }
    }
}