package edu.ucne.finanzen.data.repository

import edu.ucne.finanzen.data.local.dao.DebtDao
import edu.ucne.finanzen.data.mapper.asExternalModel
import edu.ucne.finanzen.data.mapper.toDebtRequest
import edu.ucne.finanzen.data.mapper.toEntity
import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.domain.model.Debt
import edu.ucne.finanzen.domain.model.DebtStatus
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

    override suspend fun insertDebt(debt: Debt) {
        println("DEBUG: Repositorio - Insertando deuda: ${debt.name}")


        val result = remoteDataSource.postDebt(debt.toDebtRequest())
        when (result) {
            is Resource.Success -> {
                println("DEBUG: Repositorio - API éxito, ID de API: ${result.data?.debtId}")

                val debtWithApiId = result.data?.let { apiDebt ->
                    debt.copy(debtId = apiDebt.debtId)
                } ?: debt

                debtDao.upsert(debtWithApiId.toEntity())
                println("DEBUG: Repositorio - Guardado local con ID: ${debtWithApiId.debtId}")
            }
            is Resource.Error -> {
                println("DEBUG: Repositorio - Error de API, guardando localmente con ID temporal")
                debtDao.upsert(debt.toEntity())
                throw Exception("Error de sincronización: ${result.message}")
            }
            is Resource.Loading -> {}
        }
    }

    override suspend fun updateDebt(debt: Debt) {
        println("DEBUG: Repositorio - Actualizando deuda ID local: ${debt.debtId}")
        val result = remoteDataSource.putDebt(debt.debtId, debt.toDebtRequest())
        when (result) {
            is Resource.Success -> {
                println("DEBUG: Repositorio - API éxito, actualizando localmente")
                debtDao.upsert(debt.toEntity())
            }
            is Resource.Error -> {
                println("DEBUG: Repositorio - Error de API, actualizando localmente igual")
                debtDao.upsert(debt.toEntity())
                throw Exception("Error de sincronización: ${result.message}")
            }
            is Resource.Loading -> {}
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
        println("DEBUG: Repositorio - Iniciando eliminación de deuda ID local: $id")

        // PRIMERO obtener la deuda local para saber el ID correcto
        val localDebt = debtDao.getById(id)
        println("DEBUG: Repositorio - Deuda local encontrada: $localDebt")

        if (localDebt == null) {
            println("DEBUG: Repositorio - No se encontró deuda local con ID: $id")
            throw Exception("Deuda no encontrada localmente")
        }

        val apiId = localDebt.debtId
        println("DEBUG: Repositorio - Usando ID de API: $apiId para eliminar")

        val result = remoteDataSource.deleteDebt(apiId)
        println("DEBUG: Repositorio - Respuesta de API: $result")

        when (result) {
            is Resource.Success -> {
                println("DEBUG: Repositorio - API éxito, eliminando localmente...")
                debtDao.deleteById(id)
                println("DEBUG: Repositorio - Eliminación local completada")
            }
            is Resource.Error -> {
                println("DEBUG: Repositorio - Error de API: ${result.message}")
                throw Exception("Error de sincronización: ${result.message}")
            }
            is Resource.Loading -> {
                println("DEBUG: Repositorio - Cargando...")
            }
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