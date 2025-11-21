package edu.ucne.finanzen.domain.usecases.Analytics

import edu.ucne.finanzen.domain.repository.TransactionRepository
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): Double = repository.getBalance()
}