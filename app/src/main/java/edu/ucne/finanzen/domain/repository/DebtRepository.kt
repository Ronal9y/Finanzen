package edu.ucne.finanzen.domain.repository

import edu.ucne.finanzen.domain.model.Debt
import kotlinx.coroutines.flow.Flow

interface DebtRepository {
    fun getAllDebts(usuarioId: Int): Flow<List<Debt>>
    suspend fun getDebtById(id: Int): Debt?
    suspend fun insertDebt(debt: Debt)
    suspend fun updateDebt(debt: Debt)
    suspend fun deleteDebt(debt: Debt)
    suspend fun deleteDebtById(id: Int)
    suspend fun getDebtsCount(usuarioId: Int): Int
    suspend fun getTotalRemainingDebt(usuarioId: Int): Double
    suspend fun saveDebt(debt: Debt)
    fun getActiveDebtsCount(usuarioId: Int): Flow<Int>
    fun getTotalRemainingDebtFlow(usuarioId: Int): Flow<Double>
}