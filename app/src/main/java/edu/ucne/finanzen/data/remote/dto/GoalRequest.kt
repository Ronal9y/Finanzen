package edu.ucne.finanzen.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GoalRequest(
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val deadline: String,
    val description: String,
    val usuarioId: Int
)