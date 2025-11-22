package edu.ucne.finanzen.presentation.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.finanzen.data.local.datastore.UserDataStore
import edu.ucne.finanzen.domain.usecases.Analytics.GetAverageExpenseUseCase
import edu.ucne.finanzen.domain.usecases.Analytics.GetAverageIncomeUseCase
import edu.ucne.finanzen.domain.usecases.Analytics.GetBalanceUseCase
import edu.ucne.finanzen.domain.usecases.Analytics.GetExpensesByCategoryMapUseCase
import edu.ucne.finanzen.domain.usecases.Analytics.GetTotalExpensesUseCase
import edu.ucne.finanzen.domain.usecases.Analytics.GetTotalIncomeUseCase
import edu.ucne.finanzen.domain.usecases.Analytics.GetTransactionsCountUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val getTotalIncome: GetTotalIncomeUseCase,
    private val getTotalExpenses: GetTotalExpensesUseCase,
    private val getBalance: GetBalanceUseCase,
    private val getTransactionsCount: GetTransactionsCountUseCase,
    private val getAverageExpense: GetAverageExpenseUseCase,
    private val getAverageIncome: GetAverageIncomeUseCase,
    private val getExpensesByCategoryMap: GetExpensesByCategoryMapUseCase,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(AnalysisState())
    val state: StateFlow<AnalysisState> = _state.asStateFlow()

    init {
        loadAnalysisData()
    }

    fun onEvent(event: AnalysisEvent) {
        when (event) {
            AnalysisEvent.Refresh -> loadAnalysisData()
            is AnalysisEvent.AddInsight -> { }
        }
    }

    private fun loadAnalysisData() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }

        val usuarioId = userDataStore.userIdFlow.firstOrNull()
        if (usuarioId == null) {
            _state.update {
                it.copy(
                    isLoading = false,
                    error = "No hay usuario autenticado."
                )
            }
            return@launch
        }

        getExpensesByCategoryMap(usuarioId).collect { realMap ->
            val income = getTotalIncome(usuarioId)
            val expenses = getTotalExpenses(usuarioId)
            val balance = getBalance(usuarioId)
            val savingsRate = if (income > 0) ((income - expenses) / income) * 100 else 0.0
            val topCategory = realMap.maxByOrNull { it.value }

            val insight = if (topCategory != null && expenses > 0) {
                val percentage = (topCategory.value / expenses) * 100
                "Tu mayor gasto es en ${topCategory.key}, representando el ${"%.0f".format(percentage)}% de tus gastos totales (\$${"%.2f".format(expenses)})"
            } else {
                "No hay gastos registrados aún."
            }

            _state.update {
                it.copy(
                    totalIncome = income,
                    totalExpenses = expenses,
                    balance = balance,
                    savingsRate = savingsRate,
                    transactionsCount = getTransactionsCount(usuarioId),
                    averageExpense = getAverageExpense(usuarioId),
                    averageIncome = getAverageIncome(usuarioId),
                    expensesByCategory = realMap,
                    topExpenseInsight = insight,
                    isLoading = false,
                    error = null
                )
            }
        }
    }
}