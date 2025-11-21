package edu.ucne.finanzen.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import java.time.LocalDate

@Singleton
class NotificationPrefs @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs = context.getSharedPreferences("notif_prefs", Context.MODE_PRIVATE)

    fun wasBudgetNotifiedToday(id: Int): Boolean {
        val key = "budget_$id"
        val lastDay = prefs.getString(key, "") ?: ""
        val today = LocalDate.now().toString()
        return lastDay == today
    }

    fun markBudgetNotifiedToday(id: Int) {
        prefs.edit().putString("budget_$id", LocalDate.now().toString()).apply()
    }

    fun wasDebtNotifiedToday(id: Int): Boolean {
        val key = "debt_$id"
        val lastDay = prefs.getString(key, "") ?: ""
        val today = LocalDate.now().toString()
        return lastDay == today
    }

    fun markDebtNotifiedToday(id: Int) {
        prefs.edit().putString("debt_$id", LocalDate.now().toString()).apply()
    }
}