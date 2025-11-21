package edu.ucne.finanzen.data.repository

import edu.ucne.finanzen.data.local.dao.TransactionDao
import edu.ucne.finanzen.data.mapper.asExternalModel
import edu.ucne.finanzen.data.mapper.toEntity
import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.model.TransactionType
import edu.ucne.finanzen.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.observeAll().map { list -> list.map { it.asExternalModel() } }

    override fun getTransactionsByType(type: String): Flow<List<Transaction>> =
        transactionDao.observeByType(type).map { list -> list.map { it.asExternalModel() } }

    override suspend fun getTransactionById(id: Int): Transaction? =
        transactionDao.getById(id)?.asExternalModel()

    override suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.upsert(transaction.toEntity())

    override suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.upsert(transaction.toEntity())

    override suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.delete(transaction.toEntity())

    override suspend fun deleteTransactionById(id: Int) =
        transactionDao.deleteById(id)

    override suspend fun getTotalIncome(): Double =
        transactionDao.getTotalByType(TransactionType.INCOME) ?: 0.0

    override suspend fun getTotalExpenses(): Double =
        transactionDao.getTotalByType(TransactionType.EXPENSE) ?: 0.0

    override suspend fun getBalance(): Double =
        getTotalIncome() - getTotalExpenses()

    override suspend fun getExpensesByCategory(category: String): Double =
        transactionDao.getExpensesByCategory(category) ?: 0.0

    override suspend fun getTransactionsCount(): Int =
        transactionDao.getCount()

    override suspend fun getAverageExpense(): Double {
        val count = transactionDao.getCountByType(TransactionType.EXPENSE)
        return if (count > 0) getTotalExpenses() / count else 0.0
    }

    override suspend fun getAverageIncome(): Double {
        val count = transactionDao.getCountByType(TransactionType.INCOME)
        return if (count > 0) getTotalIncome() / count else 0.0
    }
}