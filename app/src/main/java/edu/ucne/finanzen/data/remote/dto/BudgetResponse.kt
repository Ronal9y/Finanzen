package edu.ucne.finanzen.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BudgetResponse(
    val budgetId: Int,
    val category: String,
    val limit: Double,
    val spent: Double,
    val month: String,
    val alertThreshold: Int,
    val usuarioId: Int
)
