package edu.ucne.finanzen.domain.usecases.Dashboard

import edu.ucne.finanzen.domain.model.DashboardData
import  edu.ucne.finanzen.domain.usecases.Analytics.*
import  edu.ucne.finanzen.domain.usecases.Budgets.GetBudgetsCountUseCase
import  edu.ucne.finanzen.domain.usecases.Debts.GetDebtsCountUseCase
import  edu.ucne.finanzen.domain.usecases.Debts.GetTotalRemainingDebtUseCase
import  edu.ucne.finanzen.domain.usecases.Goals.GetGoalsCountUseCase
import  edu.ucne.finanzen.domain.usecases.Goals.GetTotalCompletionPercentageUseCase
import  edu.ucne.finanzen.domain.usecases.Transaction.GetTransactionsUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(
    private val getTransactions: GetTransactionsUseCase,
    private val getTotalIncome: GetTotalIncomeUseCase,
    private val getTotalExpenses: GetTotalExpensesUseCase,
    private val getBalance: GetBalanceUseCase,
    private val getGoalsCount: GetGoalsCountUseCase,
    private val getTotalCompletionPercentage: GetTotalCompletionPercentageUseCase,
    private val getDebtsCount: GetDebtsCountUseCase,
    private val getTotalRemainingDebt: GetTotalRemainingDebtUseCase,
    private val getBudgetsCount: GetBudgetsCountUseCase
) {
    suspend operator fun invoke(): DashboardData = coroutineScope {
        val transactionsAsync = async { getTransactions() }
        val totalIncomeAsync = async { getTotalIncome() }
        val totalExpensesAsync = async { getTotalExpenses() }
        val balanceAsync = async { getBalance() }
        val goalsCountAsync = async { getGoalsCount() }
        val completionPercentageAsync = async { getTotalCompletionPercentage() }
        val debtsCountAsync = async { getDebtsCount() }
        val totalRemainingDebtAsync = async { getTotalRemainingDebt() }
        val budgetsCountAsync = async { getBudgetsCount() }

        DashboardData(
            transactions = transactionsAsync.await(),
            totalIncome = totalIncomeAsync.await(),
            totalExpenses = totalExpensesAsync.await(),
            balance = balanceAsync.await(),
            goalsCount = goalsCountAsync.await(),
            completionPercentage = completionPercentageAsync.await(),
            debtsCount = debtsCountAsync.await(),
            totalRemainingDebt = totalRemainingDebtAsync.await(),
            budgetsCount = budgetsCountAsync.await()
        )
    }
}