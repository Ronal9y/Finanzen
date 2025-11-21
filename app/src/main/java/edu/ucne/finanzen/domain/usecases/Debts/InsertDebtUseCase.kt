package edu.ucne.finanzen.domain.usecases.Debts

import edu.ucne.finanzen.domain.model.Debt
import edu.ucne.finanzen.domain.repository.DebtRepository
import javax.inject.Inject

class InsertDebtUseCase @Inject constructor(
    private val repository: DebtRepository
) {
    suspend operator fun invoke(debt: Debt) = repository.insertDebt(debt)
}