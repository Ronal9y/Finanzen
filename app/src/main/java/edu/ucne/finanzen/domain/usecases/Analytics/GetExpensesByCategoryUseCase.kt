package edu.ucne.finanzen.domain.usecases.Analytics

import edu.ucne.finanzen.domain.repository.TransactionRepository
import javax.inject.Inject

class GetExpensesByCategoryUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(category: String): Double = repository.getExpensesByCategory(category)
}