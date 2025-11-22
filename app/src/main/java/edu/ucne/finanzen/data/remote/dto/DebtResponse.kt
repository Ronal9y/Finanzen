package edu.ucne.finanzen.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DebtResponse(
    val debtId: Int,
    val name: String,
    val principalAmount: Double,
    val interestRate: Double? = null,
    val interestType: String,
    val compoundingPeriod: String,
    val dueDate: String,
    val remainingAmount: Double,
    val creditor: String,
    val status: String,
    val penaltyRate: Double,
    val creationDate: String,
    val usuarioId: Int
)
