package edu.ucne.finanzen.data.repository

import edu.ucne.finanzen.data.local.dao.TransactionDao
import edu.ucne.finanzen.data.mapper.asExternalModel
import edu.ucne.finanzen.data.mapper.toEntity
import edu.ucne.finanzen.data.mapper.toRequest
import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.model.TransactionType
import edu.ucne.finanzen.domain.repository.TransactionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val remoteDataSource: RemoteDataSource
) : TransactionRepository {

    override fun getAllTransactions(usuarioId: Int): Flow<List<Transaction>> =
        transactionDao.observeAll()
            .map { list ->
                list
                    .map { it.asExternalModel() }
                    .filter { it.usuarioId == usuarioId }
            }

    override fun getTransactionsByType(
        usuarioId: Int,
        type: String
    ): Flow<List<Transaction>> =
        transactionDao.observeByType(type)
            .map { list ->
                list
                    .map { it.asExternalModel() }
                    .filter { it.usuarioId == usuarioId }
            }

    override suspend fun getTransactionById(id: Int): Transaction? =
        transactionDao.getById(id)?.asExternalModel()

    override suspend fun insertTransaction(transaction: Transaction) {
        val result = remoteDataSource.postTransaction(transaction.toRequest())
        when (result) {
            is Resource.Success -> transactionDao.upsert(transaction.toEntity())
            is Resource.Error -> {
                transactionDao.upsert(transaction.toEntity())
                throw Exception("Error de API: ${result.message}")
            }
            is Resource.Loading -> {}
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val result = remoteDataSource.putTransaction(
            transaction.transactionId,
            transaction.toRequest()
        )
        when (result) {
            is Resource.Success -> transactionDao.upsert(transaction.toEntity())
            is Resource.Error -> {
                transactionDao.upsert(transaction.toEntity())
                throw Exception("Error de API: ${result.message}")
            }
            is Resource.Loading -> {}
        }
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        val result = remoteDataSource.deleteTransaction(transaction.transactionId)
        when (result) {
            is Resource.Success -> transactionDao.delete(transaction.toEntity())
            is Resource.Error -> {
                transactionDao.delete(transaction.toEntity())
                throw Exception("Error de API: ${result.message}")
            }
            is Resource.Loading -> {}
        }
    }

    override suspend fun deleteTransactionById(id: Int) {
        val result = remoteDataSource.deleteTransaction(id)
        when (result) {
            is Resource.Success -> transactionDao.deleteById(id)
            is Resource.Error -> {
                transactionDao.deleteById(id)
                throw Exception("Error de API: ${result.message}")
            }
            is Resource.Loading -> {}
        }
    }

    private suspend fun getUserTransactionsOnce(usuarioId: Int): List<Transaction> {
        val entities = transactionDao.observeAll().first()
        return entities
            .map { it.asExternalModel() }
            .filter { it.usuarioId == usuarioId }
    }

    override suspend fun getTotalIncome(usuarioId: Int): Double {
        val userTx = getUserTransactionsOnce(usuarioId)
        return userTx
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
    }

    override suspend fun getTotalExpenses(usuarioId: Int): Double {
        val userTx = getUserTransactionsOnce(usuarioId)
        return userTx
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
    }

    override suspend fun getBalance(usuarioId: Int): Double =
        getTotalIncome(usuarioId) - getTotalExpenses(usuarioId)

    override suspend fun getExpensesByCategory(category: String): Double =
        transactionDao.getExpensesByCategory(category) ?: 0.0

    override suspend fun getTransactionsCount(): Int =
        transactionDao.getCount()

    override suspend fun getAverageExpense(): Double {
        val entities = transactionDao.observeAll().first()
        val tx = entities.map { it.asExternalModel() }
        val expenses = tx.filter { it.type == TransactionType.EXPENSE }
        return if (expenses.isNotEmpty()) {
            expenses.sumOf { it.amount } / expenses.size
        } else 0.0
    }

    override suspend fun getAverageIncome(usuarioId: Int): Double {
        val userTx = getUserTransactionsOnce(usuarioId)
        val incomes = userTx.filter { it.type == TransactionType.INCOME }
        return if (incomes.isNotEmpty()) {
            incomes.sumOf { it.amount } / incomes.size
        } else 0.0
    }
}