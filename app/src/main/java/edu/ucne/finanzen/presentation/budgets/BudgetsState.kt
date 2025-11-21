package edu.ucne.finanzen.presentation.budgets

import edu.ucne.finanzen.domain.model.Budget

data class BudgetsState(
    val budgets: List<Budget> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
