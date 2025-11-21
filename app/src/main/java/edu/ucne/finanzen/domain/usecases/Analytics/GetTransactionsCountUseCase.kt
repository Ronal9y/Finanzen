package edu.ucne.finanzen.domain.usecases.Analytics

import edu.ucne.finanzen.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsCountUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): Int = repository.getTransactionsCount()
}