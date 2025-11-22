package edu.ucne.finanzen.domain.usecases.Transaction

import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsByTypeUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(usuarioId: Int, type: String): Flow<List<Transaction>> =
        repository.getTransactionsByType(usuarioId, type)
}