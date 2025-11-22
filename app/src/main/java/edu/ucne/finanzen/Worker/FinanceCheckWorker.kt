package edu.ucne.finanzen.Worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.finanzen.common.NotificationHelper
import edu.ucne.finanzen.common.NotificationPrefs
import edu.ucne.finanzen.data.local.datastore.UserDataStore
import edu.ucne.finanzen.domain.repository.BudgetRepository
import edu.ucne.finanzen.domain.repository.DebtRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@HiltWorker
class FinanceCheckWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val budgetRepository: BudgetRepository,
    private val debtRepository: DebtRepository,
    private val userDataStore: UserDataStore,
    private val prefs: NotificationPrefs
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("FinanceCheckWorker", "Worker ejecutándose...")

        val usuarioId = userDataStore.userIdFlow.firstOrNull()
        if (usuarioId == null) {
            Log.e("FinanceCheckWorker", "Usuario no autenticado. Cancelando worker.")
            return Result.failure()
        }

        checkBudgetAlerts(usuarioId)
        checkDebtAlerts(usuarioId)

        Log.d("FinanceCheckWorker", "Worker finalizado.")
        return Result.success()
    }

    private suspend fun checkBudgetAlerts(usuarioId: Int) {
        budgetRepository.getAllBudgets(usuarioId).collect { budgets ->
            budgets.forEach { budget ->
                val used = budget.spent / budget.limit
                if (used >= budget.alertThreshold / 100.0 &&
                    !prefs.wasBudgetNotifiedToday(budget.budgetId)
                ) {
                    NotificationHelper.showBudgetAlert(
                        appContext,
                        budget.category.name,
                        (used * 100).toInt()
                    )
                    prefs.markBudgetNotifiedToday(budget.budgetId)
                }
            }
        }
    }

    private suspend fun checkDebtAlerts(usuarioId: Int) {
        debtRepository.getAllDebts(usuarioId).collect { debts ->
            val today = LocalDate.now()
            debts.forEach { debt ->
                val dueDate = LocalDate.parse(debt.dueDate)
                val daysLeft = ChronoUnit.DAYS.between(today, dueDate)

                if (daysLeft in 1..3 &&
                    debt.remainingAmount > 0 &&
                    !prefs.wasDebtNotifiedToday(debt.debtId)
                ) {
                    NotificationHelper.showDebtDueSoonAlert(
                        appContext,
                        debt.name,
                        daysLeft.toInt()
                    )
                    prefs.markDebtNotifiedToday(debt.debtId)
                }
            }
        }
    }
}