package edu.ucne.finanzen.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.finanzen.domain.model.Budget
import edu.ucne.finanzen.domain.model.Debt
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefs: NotificationPrefs
) {

    fun checkBudgetNow(budget: Budget) {
        val used = budget.spent / budget.limit
        val percentage = (used * 100).toInt()
        if (used >= budget.alertThreshold / 100.0 && !prefs.wasBudgetNotifiedToday(budget.budgetId)) {
            NotificationHelper.showBudgetAlert(context, budget.category.name, percentage)
            prefs.markBudgetNotifiedToday(budget.budgetId)
        }
        // Si bajó por debajo del umbral -> resetear flag
        if (used < budget.alertThreshold / 100.0) {
            prefs.resetBudgetNotified(budget.budgetId)
        }
    }

    fun checkDebtNow(debt: Debt) {
        val daysLeft = daysBetween(LocalDate.now(), LocalDate.parse(debt.dueDate.take(10)))
        if (daysLeft in 1..3 && debt.remainingAmount > 0 && !prefs.wasDebtNotifiedToday(debt.debtId)) {
            NotificationHelper.showDebtDueSoonAlert(context, debt.name, daysLeft.toInt())
            prefs.markDebtNotifiedToday(debt.debtId)
        }
        // Si ya no cumple condición -> resetear flag
        if (daysLeft !in 1..3 || debt.remainingAmount <= 0) {
            prefs.resetDebtNotified(debt.debtId)
        }
    }

    private fun daysBetween(start: LocalDate, end: LocalDate): Long =
        ChronoUnit.DAYS.between(start, end)
}