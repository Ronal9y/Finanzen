package edu.ucne.finanzen.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.finanzen.data.local.datastore.UserDataStore
import edu.ucne.finanzen.domain.usecases.Dashboard.GetDashboardDataUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardData: GetDashboardDataUseCase,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboard()
    }

    fun onEvent(event: DashboardEvent) {
        if (event == DashboardEvent.Refresh) {
            loadDashboard()
        }
    }

    private fun loadDashboard() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }

        val usuarioId = userDataStore.userIdFlow.firstOrNull()
        if (usuarioId == null) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "Usuario no autenticado"
                )
            }
            return@launch
        }

        try {
            val data = getDashboardData(usuarioId)

            data.transactions.collectLatest { transactions ->
                _state.update { current ->
                    current.copy(
                        totalIncome = data.totalIncome,
                        totalExpenses = data.totalExpenses,
                        balance = data.balance,
                        goalsCount = data.goalsCount,
                        completionPercentage = data.completionPercentage,
                        debtsCount = data.debtsCount,
                        totalRemainingDebt = data.totalRemainingDebt,
                        budgetsCount = data.budgetsCount,
                        recentTransactions = transactions.take(5),
                        isLoading = false,
                        error = null
                    )
                }
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar datos"
                )
            }
        }
    }
}