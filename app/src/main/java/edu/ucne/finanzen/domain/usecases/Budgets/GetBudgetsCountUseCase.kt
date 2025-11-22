package edu.ucne.finanzen.domain.usecases.Budgets

import edu.ucne.finanzen.domain.repository.BudgetRepository
import javax.inject.Inject

class GetBudgetsCountUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(usuarioId: Int): Int =
        repository.getBudgetsCount(usuarioId)
}