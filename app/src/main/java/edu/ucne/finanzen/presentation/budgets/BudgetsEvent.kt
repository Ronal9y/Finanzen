package edu.ucne.finanzen.presentation.budgets

import edu.ucne.finanzen.domain.model.CategoryType

sealed interface BudgetsEvent {
    data object Refresh : BudgetsEvent
    data class DeleteBudget(val id: Int) : BudgetsEvent
    data class AddBudget(
        val category: CategoryType,
        val limit: Double,
        val alertThreshold: Int
    ) : BudgetsEvent
}