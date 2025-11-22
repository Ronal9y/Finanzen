package edu.ucne.finanzen.domain.usecases.Analytics

import edu.ucne.finanzen.domain.repository.TransactionRepository
import javax.inject.Inject

class GetAverageExpenseUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(usuarioId: Int): Double = repository.getAverageExpense()
}