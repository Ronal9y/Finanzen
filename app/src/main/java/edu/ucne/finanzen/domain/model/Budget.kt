package edu.ucne.finanzen.domain.model

data class Budget(
    val budgetId: Int = 0,
    val category: CategoryType,
    val limit: Double,
    val spent: Double = 0.0,
    val month: String,
    val usuarioId: Int,
    val alertThreshold: Int = 80
)
