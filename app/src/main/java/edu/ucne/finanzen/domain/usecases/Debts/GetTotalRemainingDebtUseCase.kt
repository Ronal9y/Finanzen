package edu.ucne.finanzen.domain.usecases.Debts

import edu.ucne.finanzen.domain.repository.DebtRepository
import javax.inject.Inject

class GetTotalRemainingDebtUseCase @Inject constructor(
    private val repository: DebtRepository
) {
    suspend operator fun invoke(usuarioId: Int): Double =
        repository.getTotalRemainingDebt(usuarioId)
}