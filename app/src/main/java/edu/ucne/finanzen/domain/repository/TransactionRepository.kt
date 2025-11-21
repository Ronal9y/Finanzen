package edu.ucne.finanzen.domain.repository

import edu.ucne.finanzen.domain.model.Transaction
import kotlinx.coroutines.flow.Flow
interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByType(type: String): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Int): Transaction?
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun deleteTransactionById(id: Int)

    // Analytics
    suspend fun getTotalIncome(): Double
    suspend fun getTotalExpenses(): Double
    suspend fun getBalance(): Double
    suspend fun getExpensesByCategory(category: String): Double
    suspend fun getTransactionsCount(): Int
    suspend fun getAverageExpense(): Double
    suspend fun getAverageIncome(): Double
}