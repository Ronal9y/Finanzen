package edu.ucne.finanzen.domain.usecases.Budgets

import edu.ucne.finanzen.domain.model.TransactionType
import edu.ucne.finanzen.domain.repository.BudgetRepository
import edu.ucne.finanzen.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateBudgetSpentUseCase @Inject constructor(
    private val budgetRepo: BudgetRepository,
    private val transactionRepo: TransactionRepository
) {
    suspend operator fun invoke(usuarioId: Int) {
        val budgets = budgetRepo.getAllBudgets(usuarioId).first()

        val expenses = transactionRepo.getAllTransactions(usuarioId)
            .first()
            .filter { it.type == TransactionType.EXPENSE }

        val spentByCategory = expenses.groupBy { it.category }
            .mapValues { (_, list) -> list.sumOf { it.amount } }

        budgets.forEach { budget ->
            val spent = spentByCategory[budget.category] ?: 0.0
            val updated = budget.copy(spent = spent)
            budgetRepo.updateBudget(updated)
        }
    }
}