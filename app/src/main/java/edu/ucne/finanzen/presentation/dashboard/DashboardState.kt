package edu.ucne.finanzen.presentation.dashboard

import edu.ucne.finanzen.domain.model.Transaction

data class DashboardState(
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val balance: Double = 0.0,
    val goalsCount: Int = 0,
    val completionPercentage: Double = 0.0,
    val debtsCount: Int = 0,
    val totalRemainingDebt: Double = 0.0,
    val budgetsCount: Int = 0,
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)