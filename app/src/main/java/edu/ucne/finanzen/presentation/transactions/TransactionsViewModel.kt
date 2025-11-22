package edu.ucne.finanzen.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.finanzen.data.local.datastore.UserDataStore
import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.model.TransactionType
import edu.ucne.finanzen.domain.usecases.Budgets.UpdateBudgetSpentUseCase
import edu.ucne.finanzen.domain.usecases.Transaction.DeleteTransactionByIdUseCase
import edu.ucne.finanzen.domain.usecases.Transaction.GetTransactionsByTypeUseCase
import edu.ucne.finanzen.domain.usecases.Transaction.GetTransactionsUseCase
import edu.ucne.finanzen.domain.usecases.Transaction.InsertTransactionUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactions: GetTransactionsUseCase,
    private val getTransactionsByType: GetTransactionsByTypeUseCase,
    private val deleteTransactionById: DeleteTransactionByIdUseCase,
    private val updateBudgetSpent: UpdateBudgetSpentUseCase,
    private val insertTransaction: InsertTransactionUseCase,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionsState())
    val state: StateFlow<TransactionsState> = _state.asStateFlow()

    init {
        loadCurrentUserAndTransactions()
    }

    private fun loadCurrentUserAndTransactions() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        try {
            val userId = userDataStore.userIdFlow.firstOrNull()

            // Actualizar el estado con el userId
            _state.update { it.copy(currentUserId = userId) }

            if (userId == null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Usuario no autenticado"
                    )
                }
                return@launch
            }

            loadTransactions(userId, _state.value.selectedFilter)

        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Error al cargar usuario: ${e.message}"
                )
            }
        }
    }

    fun onEvent(event: TransactionsEvent) {
        when (event) {
            is TransactionsEvent.FilterChanged -> {
                _state.update { it.copy(selectedFilter = event.filter) }
                reloadTransactionsWithCurrentFilter()
            }
            is TransactionsEvent.DeleteTransaction -> {
                deleteTransaction(event.id)
            }
            is TransactionsEvent.AddTransaction -> {
                addTransaction(event.transaction)
            }
            TransactionsEvent.Refresh -> {
                reloadTransactionsWithCurrentFilter()
            }
        }
    }

    private fun reloadTransactionsWithCurrentFilter() = viewModelScope.launch {
        val userId = userDataStore.userIdFlow.firstOrNull() ?: return@launch
        loadTransactions(userId, _state.value.selectedFilter)
    }

    private fun loadTransactions(userId: Int, filter: String = "Todas") = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }

        try {
            val transactionsFlow = when (filter) {
                "Ingresos" -> getTransactionsByType(userId, TransactionType.INCOME.name)
                "Gastos" -> getTransactionsByType(userId, TransactionType.EXPENSE.name)
                else -> getTransactions(userId)
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
            _state.update {
                it.copy(
                    error = "Error al cargar transacciones: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    private fun deleteTransaction(id: Int) = viewModelScope.launch {
        try {
            deleteTransactionById(id)
            reloadTransactionsWithCurrentFilter()
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }

    private fun addTransaction(transaction: Transaction) = viewModelScope.launch {
        try {
            val userId = userDataStore.userIdFlow.firstOrNull() ?: return@launch
            val txWithUser = transaction.copy(usuarioId = userId)
            insertTransaction(txWithUser)

            if (txWithUser.type == TransactionType.EXPENSE) {
                updateBudgetSpent(userId)
            }

            reloadTransactionsWithCurrentFilter()
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message) }
        }
    }
}