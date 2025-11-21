package edu.ucne.finanzen.Worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import edu.ucne.finanzen.common.NotificationHelper
import edu.ucne.finanzen.common.NotificationPrefs
import edu.ucne.finanzen.domain.repository.BudgetRepository
import edu.ucne.finanzen.domain.repository.DebtRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@HiltWorker
class FinanceCheckWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val budgetRepository: BudgetRepository,
    private val debtRepository: DebtRepository,
    private val prefs: NotificationPrefs
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("FinanceCheckWorker", "Worker ejecutándose...")
        checkBudgetAlerts()
        checkDebtAlerts()
        Log.d("FinanceCheckWorker", "Worker finalizado.")
        return Result.success()
    }

    private suspend fun checkBudgetAlerts() {
        budgetRepository.getAllBudgets().collect { budgets ->
            budgets.forEach { budget ->
                val used = budget.spent / budget.limit
                if (used >= budget.alertThreshold / 100.0 && !prefs.wasBudgetNotifiedToday(budget.budgetId)) {
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

    private suspend fun checkDebtAlerts() {
        debtRepository.getAllDebts().collect { debts ->
            val today = LocalDate.now()
            debts.forEach { debt ->
                val dueDate = LocalDate.parse(debt.dueDate)
                val daysLeft = ChronoUnit.DAYS.between(today, dueDate)

                if (daysLeft in 1..3 && debt.remainingAmount > 0 && !prefs.wasDebtNotifiedToday(debt.debtId)) {
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