package edu.ucne.finanzen.data.repository

import edu.ucne.finanzen.data.local.dao.DebtDao
import edu.ucne.finanzen.data.mapper.asExternalModel
import edu.ucne.finanzen.data.mapper.toDebtRequest
import edu.ucne.finanzen.data.mapper.toEntity
import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.domain.model.Debt
import edu.ucne.finanzen.domain.model.DebtStatus
import edu.ucne.finanzen.data.remote.dto.DebtResponse
import edu.ucne.finanzen.domain.repository.DebtRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DebtRepositoryImpl @Inject constructor(
    private val debtDao: DebtDao,
    private val remoteDataSource: RemoteDataSource
) : DebtRepository {

    override fun getAllDebts(usuarioId: Int): Flow<List<Debt>> =
        debtDao.observeAll().map { list ->
            list.map { it.asExternalModel() }
                .filter { it.usuarioId == usuarioId }
        }

    override suspend fun getDebtById(id: Int): Debt? =
        debtDao.getById(id)?.asExternalModel()

    override suspend fun upsertDebt(debt: Debt) {
        val result: Resource<DebtResponse> = (if (debt.debtId == 0) {
            remoteDataSource.postDebt(debt.toDebtRequest())
        } else {
            remoteDataSource.putDebt(debt.debtId, debt.toDebtRequest())
        }) as Resource<DebtResponse>

        when (result) {
            is Resource.Success<DebtResponse> -> {
                // ← clave: actualiza el ID local con el que devuelve la API
                val apiId = result.data?.debtId ?: debt.debtId
                debtDao.upsert(debt.copy(debtId = apiId).toEntity())
            }
            is Resource.Error<DebtResponse> -> {
                // offline: guardamos con el ID que ya tenía
                debtDao.upsert(debt.toEntity())
            }
            is Resource.Loading<DebtResponse> -> {}
        }
    }

    override suspend fun deleteDebt(debt: Debt) {
        println("DEBUG: Repositorio - Eliminando deuda ID local: ${debt.debtId}")
        val result = remoteDataSource.deleteDebt(debt.debtId)
        when (result) {
            is Resource.Success -> {
                println("DEBUG: Repositorio - API éxito, eliminando localmente")
                debtDao.delete(debt.toEntity())
            }
            is Resource.Error -> {
                println("DEBUG: Repositorio - Error de API: ${result.message}")
                throw Exception("Error de sincronización: ${result.message}")
            }
            is Resource.Loading -> {}
        }
    }

    override suspend fun deleteDebtById(id: Int) {
        val localDebt = debtDao.getById(id) ?: return
        val result = remoteDataSource.deleteDebt(localDebt.debtId) // ← ID real
        when (result) {
            is Resource.Success -> debtDao.deleteById(id)
            is Resource.Error -> debtDao.deleteById(id) // también en local
            is Resource.Loading -> {}
        }
    }

    override suspend fun getDebtsCount(usuarioId: Int): Int =
        debtDao.observeAll().map { list ->
            list.map { it.asExternalModel() }
                .filter { it.usuarioId == usuarioId }
                .size
        }.first()

    override suspend fun getTotalRemainingDebt(usuarioId: Int): Double =
        debtDao.observeAll().map { list ->
            list.map { it.asExternalModel() }
                .filter { it.usuarioId == usuarioId }
                .sumOf { it.remainingAmount }
        }.first()

    override suspend fun saveDebt(debt: Debt) =
        debtDao.upsert(debt.toEntity())

    override fun getActiveDebtsCount(usuarioId: Int): Flow<Int> =
        debtDao.observeAll().map { list ->
            list.map { it.asExternalModel() }
                .filter { it.usuarioId == usuarioId && it.status == DebtStatus.ACTIVE }
                .size
        }

    override fun getTotalRemainingDebtFlow(usuarioId: Int): Flow<Double> =
        debtDao.observeAll().map { list ->
            list.map { it.asExternalModel() }
                .filter { it.usuarioId == usuarioId }
                .sumOf { it.remainingAmount }
        }
}