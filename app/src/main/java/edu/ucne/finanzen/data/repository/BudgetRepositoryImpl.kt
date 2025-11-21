package edu.ucne.finanzen.data.repository

import edu.ucne.finanzen.data.local.dao.BudgetDao
import edu.ucne.finanzen.data.mapper.asExternalModel
import edu.ucne.finanzen.data.mapper.toEntity
import edu.ucne.finanzen.domain.model.Budget
import edu.ucne.finanzen.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao
) : BudgetRepository {

    override fun getAllBudgets(): Flow<List<Budget>> =
        budgetDao.observeAll().map { list -> list.map { it.asExternalModel() } }

    override suspend fun getBudgetById(id: Int): Budget? =
        budgetDao.getById(id)?.asExternalModel()

    override suspend fun insertBudget(budget: Budget) =
        budgetDao.upsert(budget.toEntity())

    override suspend fun updateBudget(budget: Budget) =
        budgetDao.upsert(budget.toEntity())

    override suspend fun deleteBudget(budget: Budget) =
        budgetDao.delete(budget.toEntity())

    override suspend fun deleteBudgetById(id: Int) =
        budgetDao.deleteById(id)

    override suspend fun getBudgetsCount(): Int =
        budgetDao.getCount()
}