package edu.ucne.finanzen.domain.usecases.Budgets

import edu.ucne.finanzen.domain.model.Budget
import edu.ucne.finanzen.domain.repository.BudgetRepository
import javax.inject.Inject

class DeleteBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget) = repository.deleteBudget(budget)
}