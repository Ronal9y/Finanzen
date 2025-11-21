package edu.ucne.finanzen.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.finanzen.domain.usecases.Dashboard.GetDashboardDataUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardData: GetDashboardDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboard()
    }

    fun onEvent(event: DashboardEvent) {
        if (event == DashboardEvent.Refresh) loadDashboard()
    }

    private fun loadDashboard() = viewModelScope.launch {
        try {
            _state.update { it.copy(isLoading = true, error = null) }

            val data = getDashboardData()

            data.transactions.collectLatest { transactions ->
                _state.update {
                    DashboardState(
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