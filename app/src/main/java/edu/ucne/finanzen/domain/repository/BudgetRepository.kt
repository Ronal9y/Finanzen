package edu.ucne.finanzen.domain.repository

import edu.ucne.finanzen.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAllBudgets(usuarioId: Int): Flow<List<Budget>>
    suspend fun getBudgetById(id: Int): Budget?
    suspend fun upsertBudget(budget: Budget)
    suspend fun deleteBudget(budget: Budget)
    suspend fun deleteBudgetById(id: Int)
    suspend fun getBudgetsCount(usuarioId: Int): Int
}