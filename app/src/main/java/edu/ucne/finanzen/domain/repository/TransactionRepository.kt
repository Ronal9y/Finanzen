package edu.ucne.finanzen.domain.repository

import edu.ucne.finanzen.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(usuarioId: Int): Flow<List<Transaction>>
    fun getTransactionsByType(usuarioId: Int, type: String): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Int): Transaction?
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transaction: Transaction)
    suspend fun deleteTransactionById(id: Int)
    suspend fun getTotalIncome(usuarioId: Int): Double
    suspend fun getTotalExpenses(usuarioId: Int): Double
    suspend fun getBalance(usuarioId: Int): Double
    suspend fun getExpensesByCategory(category: String): Double
    suspend fun getTransactionsCount(): Int
    suspend fun getAverageExpense(): Double
    suspend fun getAverageIncome(usuarioId: Int): Double
}