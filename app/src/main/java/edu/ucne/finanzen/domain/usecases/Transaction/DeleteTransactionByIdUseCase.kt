package edu.ucne.finanzen.domain.usecases.Transaction

import edu.ucne.finanzen.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionByIdUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteTransactionById(id)
}