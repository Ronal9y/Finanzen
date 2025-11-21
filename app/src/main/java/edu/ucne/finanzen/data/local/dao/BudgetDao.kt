package edu.ucne.finanzen.data.local.dao

import androidx.room.*
import edu.ucne.finanzen.data.local.entity.BudgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets ORDER BY month DESC")
    fun observeAll(): Flow<List<BudgetEntity>>

    @Query("SELECT * FROM budgets WHERE budgetId = :id LIMIT 1")
    suspend fun getById(id: Int): BudgetEntity?

    @Upsert
    suspend fun upsert(budget: BudgetEntity)

    @Delete
    suspend fun delete(budget: BudgetEntity)

    @Query("DELETE FROM budgets WHERE budgetId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM budgets")
    suspend fun getCount(): Int
}