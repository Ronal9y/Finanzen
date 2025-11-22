package edu.ucne.finanzen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "debts")
data class DebtEntity(
    @PrimaryKey(autoGenerate = true)
    val debtId: Int = 0,
    val name: String,
    val principalAmount: Double,
    val interestRate: Double?,           // null = 0 interés
    val interestType: String,            // "SIMPLE", "COMPOUND"
    val compoundingPeriod: String,       // "DAILY", "WEEKLY"...
    val dueDate: String,                 // "yyyy-MM-dd"
    val remainingAmount: Double,
    val creditor: String,
    val status: String,                  // "ACTIVE"...
    val penaltyRate: Double = 5.0,
    val usuarioId: Int,
    val creationDate: String
)