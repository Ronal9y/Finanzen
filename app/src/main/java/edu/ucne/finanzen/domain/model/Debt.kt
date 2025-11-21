package edu.ucne.finanzen.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class Debt(
    val debtId: Int = 0,
    val name: String,                    // Nombre préstamo
    val principalAmount: Double,         // Monto original
    val interestRate: Double?,           // Anual (%). Null = sin interés
    val interestType: InterestType,      // Simple o Compuesto
    val compoundingPeriod: CompoundingPeriod,
    val dueDate: String,                 // "yyyy-MM-dd"
    val remainingAmount: Double,         // Monto actualizado
    val creditor: String,
    val status: DebtStatus,
    val penaltyRate: Double = 5.0,       // Penalización por mora (%)
    val creationDate: String = getCurrentDate() // Fecha de creación
) {
    companion object {
        fun getCurrentDate(): String {
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        }
    }
}

enum class CompoundingPeriod { DAILY, WEEKLY, MONTHLY, YEARLY }
enum class DebtStatus { ACTIVE, PAID, OVERDUE, RENEWED }
enum class InterestType { SIMPLE, COMPOUND }
