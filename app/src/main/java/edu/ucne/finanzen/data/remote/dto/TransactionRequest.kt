package edu.ucne.finanzen.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest(
    val type: String,
    val amount: Double,
    val category: String,
    val description: String,
    val date: String,
    val usuarioId: Int
)
