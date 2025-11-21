package edu.ucne.finanzen.domain.usecases.Analytics

import edu.ucne.finanzen.domain.repository.TransactionRepository
import javax.inject.Inject

class GetAverageIncomeUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): Double = repository.getAverageIncome()
}