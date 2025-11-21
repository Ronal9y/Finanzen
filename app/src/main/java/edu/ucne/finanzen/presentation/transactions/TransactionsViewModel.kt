package edu.ucne.finanzen.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.model.TransactionType
import edu.ucne.finanzen.domain.usecases.Transaction.*
import edu.ucne.finanzen.domain.usecases.Budgets.UpdateBudgetSpentUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactions: GetTransactionsUseCase,
    private val getTransactionsByType: GetTransactionsByTypeUseCase,
    private val deleteTransactionById: DeleteTransactionByIdUseCase,
    private val updateBudgetSpent: UpdateBudgetSpentUseCase,
    private val insertTransaction: InsertTransactionUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state.asStateFlow()

    init {
        loadTransactions()
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.FilterChanged -> {
                loadTransactions(event.filter)
            }
            is TransactionsEvent.DeleteTransaction -> {
                deleteTransaction(event.id)
            }
            is TransactionsEvent.AddTransaction -> {
                addTransaction(event.transaction)
            }
            TransactionsEvent.Refresh -> {
                loadTransactions(_state.value.selectedFilter)
            }
        }
    }

    private fun loadTransactions(filter: String = "Todas") = viewModelScope.launch {
        try {
            val transactionsFlow = when (filter) {
                "Ingresos" -> getTransactionsByType(TransactionType.INCOME.name)
                "Gastos" -> getTransactionsByType(TransactionType.EXPENSE.name)
                else -> getTransactions()
            }

            transactionsFlow.collect { transactions ->
                _state.update {
                    it.copy(
                        transactions = transactions,
                        selectedFilter = filter,
                        isLoading = false,
                        error = null
                    )
                }
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message, isLoading = false) }
        }
    }

    private fun deleteTransaction(id: Int) = viewModelScope.launch {
        try {
            deleteTransactionById(id)
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }

    private fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        try {
            insertTransaction(transaction)

            if (transaction.type == TransactionType.EXPENSE) {
                updateBudgetSpent()
            }
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }

}