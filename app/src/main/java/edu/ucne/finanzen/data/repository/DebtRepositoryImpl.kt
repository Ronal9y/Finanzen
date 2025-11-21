package edu.ucne.finanzen.data.repository

import edu.ucne.finanzen.data.local.dao.DebtDao
import edu.ucne.finanzen.data.mapper.asExternalModel
import edu.ucne.finanzen.data.mapper.toEntity
import edu.ucne.finanzen.domain.model.Debt
import edu.ucne.finanzen.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DebtRepositoryImpl @Inject constructor(
    private val debtDao: DebtDao
) : DebtRepository {

    override fun getAllDebts(): Flow<List<Debt>> =
        debtDao.observeAll().map { list -> list.map { it.asExternalModel() } }

    override suspend fun getDebtById(id: Int): Debt? =
        debtDao.getById(id)?.asExternalModel()

    override suspend fun insertDebt(debt: Debt) =
        debtDao.upsert(debt.toEntity())

    override suspend fun updateDebt(debt: Debt) =
        debtDao.upsert(debt.toEntity())

    override suspend fun deleteDebt(debt: Debt) =
        debtDao.delete(debt.toEntity())

    override suspend fun deleteDebtById(id: Int) =
        debtDao.deleteById(id)

    override suspend fun getDebtsCount(): Int =
        debtDao.getCount()

    override suspend fun getTotalRemainingDebt(): Double =
        debtDao.getTotalRemainingFlow().first() ?: 0.0

    override suspend fun saveDebt(debt: Debt) =
        debtDao.upsert(debt.toEntity())

    override fun getActiveDebtsCount(): Flow<Int> =
        debtDao.getActiveCountFlow()

    override suspend fun getTotalRemainingDebtFlow(): Flow<Double> =
        debtDao.getTotalRemainingFlow().map { it ?: 0.0 }
}