package edu.ucne.finanzen.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BudgetRequest(
    val category: String,
    val limit: Double,
    val spent: Double,
    val month: String,
    val alertThreshold: Int = 80,
    val usuarioId: Int
)