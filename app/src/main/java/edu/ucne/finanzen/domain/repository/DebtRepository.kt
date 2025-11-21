package edu.ucne.finanzen.domain.repository

import edu.ucne.finanzen.domain.model.Debt
import kotlinx.coroutines.flow.Flow

interface DebtRepository {
    fun getAllDebts(): Flow<List<Debt>>
    suspend fun getDebtById(id: Int): Debt?
    suspend fun insertDebt(debt: Debt)
    suspend fun updateDebt(debt: Debt)
    suspend fun deleteDebt(debt: Debt)
    suspend fun deleteDebtById(id: Int)
    suspend fun getDebtsCount(): Int
    suspend fun getTotalRemainingDebt(): Double
    suspend fun saveDebt(debt: Debt)

    fun getActiveDebtsCount(): Flow<Int>

    suspend fun getTotalRemainingDebtFlow(): Flow<Double>
}