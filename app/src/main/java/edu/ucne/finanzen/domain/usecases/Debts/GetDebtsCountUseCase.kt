package edu.ucne.finanzen.domain.usecases.Debts

import edu.ucne.finanzen.domain.repository.DebtRepository
import javax.inject.Inject

class GetDebtsCountUseCase @Inject constructor(
    private val repository: DebtRepository
) {
    suspend operator fun invoke(usuarioId: Int): Int =
        repository.getDebtsCount(usuarioId)
}