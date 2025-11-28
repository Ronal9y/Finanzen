package edu.ucne.finanzen.data.repository

import edu.ucne.finanzen.data.local.dao.BudgetDao
import edu.ucne.finanzen.data.mapper.asExternalModel
import edu.ucne.finanzen.data.mapper.toBudgetRequest
import edu.ucne.finanzen.data.mapper.toEntity
import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.domain.model.Budget
import edu.ucne.finanzen.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val remoteDataSource: RemoteDataSource
) : BudgetRepository {

    override fun getAllBudgets(usuarioId: Int): Flow<List<Budget>> =
        budgetDao.observeAll().map { list -> list.map { it.asExternalModel() } }

    override suspend fun getBudgetById(id: Int): Budget? =
        budgetDao.getById(id)?.asExternalModel()

    override suspend fun insertBudget(budget: Budget) {
        val result = remoteDataSource.postBudget(budget.toBudgetRequest())
        when (result) {
            is Resource.Success -> {
                budgetDao.upsert(budget.toEntity())
            }
            is Resource.Error -> {
                budgetDao.upsert(budget.toEntity())
            }
            is Resource.Loading -> {}
        }
    }

    override suspend fun updateBudget(budget: Budget) {
        val result = remoteDataSource.putBudget(budget.budgetId, budget.toBudgetRequest())
        when (result) {
            is Resource.Success -> {
                budgetDao.upsert(budget.toEntity())
            }
            is Resource.Error -> {
                budgetDao.upsert(budget.toEntity())
            }
            is Resource.Loading -> {}
        }
    }

    override suspend fun deleteBudget(budget: Budget) {
        val result = remoteDataSource.deleteBudget(budget.budgetId)
        when (result) {
            is Resource.Success -> {
                budgetDao.delete(budget.toEntity())
            }
            is Resource.Error -> {
                budgetDao.delete(budget.toEntity())
            }
            is Resource.Loading -> {}
        }
    }

    override suspend fun deleteBudgetById(id: Int) {
        val result = remoteDataSource.deleteBudget(id)
        when (result) {
            is Resource.Success -> {
                budgetDao.deleteById(id)
            }
            is Resource.Error -> {
                budgetDao.deleteById(id)
            }
            is Resource.Loading -> {}
        }
    }

    override suspend fun getBudgetsCount(usuarioId: Int): Int =
        budgetDao.getCount()
}