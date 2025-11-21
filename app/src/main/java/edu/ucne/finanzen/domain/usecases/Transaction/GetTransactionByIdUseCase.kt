package edu.ucne.finanzen.domain.usecases.Transaction

import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Int): Transaction? = repository.getTransactionById(id)
}