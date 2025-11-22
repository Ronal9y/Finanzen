package edu.ucne.finanzen.presentation.transactions

import edu.ucne.finanzen.domain.model.Transaction

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val selectedFilter: String = "Todas",
    val isLoading: Boolean = true,
    val error: String? = null,
    val currentUserId: Int? = null
)
