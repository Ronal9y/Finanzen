package edu.ucne.finanzen.domain.usecases.Debts

import edu.ucne.finanzen.domain.repository.DebtRepository
import javax.inject.Inject

class DeleteDebtByIdUseCase @Inject constructor(
    private val repository: DebtRepository
) {
    suspend operator fun invoke(id: Int) = repository.deleteDebtById(id)
}