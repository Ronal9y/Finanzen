package edu.ucne.finanzen.presentation.analysis

data class AnalysisState(
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val balance: Double = 0.0,
    val savingsRate: Double = 0.0,
    val transactionsCount: Int = 0,
    val averageExpense: Double = 0.0,
    val averageIncome: Double = 0.0,
    val expensesByCategory: Map<String, Double> = emptyMap(),
    val topExpenseInsight: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)
