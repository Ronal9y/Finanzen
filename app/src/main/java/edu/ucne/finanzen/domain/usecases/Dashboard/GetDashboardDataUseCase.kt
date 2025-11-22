package edu.ucne.finanzen.domain.usecases.Dashboard

import edu.ucne.finanzen.domain.model.DashboardData
import edu.ucne.finanzen.domain.usecases.Analytics.GetBalanceUseCase
import edu.ucne.finanzen.domain.usecases.Analytics.GetTotalExpensesUseCase
import edu.ucne.finanzen.domain.usecases.Analytics.GetTotalIncomeUseCase
import edu.ucne.finanzen.domain.usecases.Budgets.GetBudgetsCountUseCase
import edu.ucne.finanzen.domain.usecases.Debts.GetDebtsCountUseCase
import edu.ucne.finanzen.domain.usecases.Debts.GetTotalRemainingDebtUseCase
import edu.ucne.finanzen.domain.usecases.Goals.GetGoalsCountUseCase
import edu.ucne.finanzen.domain.usecases.Goals.GetTotalCompletionPercentageUseCase
import edu.ucne.finanzen.domain.usecases.Transaction.GetTransactionsUseCase
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

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
    suspend operator fun invoke(usuarioId: Int): DashboardData = coroutineScope {
        val transactionsAsync = async { getTransactions(usuarioId) }
        val totalIncomeAsync = async { getTotalIncome(usuarioId) }
        val totalExpensesAsync = async { getTotalExpenses(usuarioId) }
        val balanceAsync = async { getBalance(usuarioId) }
        val goalsCountAsync = async { getGoalsCount(usuarioId) }
        val completionPercentageAsync = async { getTotalCompletionPercentage(usuarioId) }
        val debtsCountAsync = async { getDebtsCount(usuarioId) }
        val totalRemainingDebtAsync = async { getTotalRemainingDebt(usuarioId) }
        val budgetsCountAsync = async { getBudgetsCount(usuarioId) }

        DashboardData(
            transactions = transactionsAsync.await(),
            totalIncome = totalIncomeAsync.await(),
            totalExpenses = totalExpensesAsync.await(),
            balance = balanceAsync.await(),
            goalsCount = goalsCountAsync.await(),
            completionPercentage = completionPercentageAsync.await(),
            debtsCount = debtsCountAsync.await(),
            totalRemainingDebt = totalRemainingDebtAsync.await(),
            usuarioId = usuarioId,
            budgetsCount = budgetsCountAsync.await()
        )
    }
}