package edu.ucne.finanzen.domain.model

data class DashboardData(
    val transactions: kotlinx.coroutines.flow.Flow<List< edu.ucne.finanzen.domain.model.Transaction>>,
    val totalIncome: Double,
    val totalExpenses: Double,
    val balance: Double,
    val goalsCount: Int,
    val completionPercentage: Double,
    val debtsCount: Int,
    val totalRemainingDebt: Double,
    val usuarioId: Int,
    val budgetsCount: Int
)
