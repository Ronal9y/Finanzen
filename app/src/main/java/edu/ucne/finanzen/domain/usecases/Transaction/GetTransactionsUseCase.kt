package edu.ucne.finanzen.domain.usecases.Transaction

import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<Transaction>> = repository.getAllTransactions()
}