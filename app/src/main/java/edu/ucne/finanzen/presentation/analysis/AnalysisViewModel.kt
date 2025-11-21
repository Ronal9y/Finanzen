package edu.ucne.finanzen.presentation.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.finanzen.domain.model.CategoryType
import edu.ucne.finanzen.domain.model.TransactionType
import edu.ucne.finanzen.domain.usecases.Analytics.*
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val getTotalIncome: GetTotalIncomeUseCase,
    private val getTotalExpenses: GetTotalExpensesUseCase,
    private val getBalance: GetBalanceUseCase,
    private val getTransactionsCount: GetTransactionsCountUseCase,
    private val getAverageExpense: GetAverageExpenseUseCase,
    private val getAverageIncome: GetAverageIncomeUseCase,
    private val getExpensesByCategoryMap: GetExpensesByCategoryMapUseCase // ⭐ nuevo
) : ViewModel() {

    private val _state = MutableStateFlow(AnalysisState())
    val state: StateFlow<AnalysisState> = _state.asStateFlow()

    init {
        loadAnalysisData()
    }

    fun onEvent(event: AnalysisEvent) {
        when (event) {
            AnalysisEvent.Refresh -> loadAnalysisData()
            is AnalysisEvent.AddInsight -> { /* reservado */ }
        }
    }

    private fun loadAnalysisData() = viewModelScope.launch {
        getExpensesByCategoryMap().collect { realMap ->
            val income = getTotalIncome()
            val expenses = getTotalExpenses()
            val balance = getBalance()
            val savingsRate = if (income > 0) ((income - expenses) / income) * 100 else 0.0
            val topCategory = realMap.maxByOrNull { it.value }
            val insight = if (topCategory != null && expenses > 0) {
                val percentage = (topCategory.value / expenses) * 100
                "Tu mayor gasto es en ${topCategory.key}, representando el ${"%.0f".format(percentage)}% de tus gastos totales (\$${"%.2f".format(expenses)})"
            } else {
                "No hay gastos registrados aún."
            }

            _state.update {
                AnalysisState(
                    totalIncome = income,
                    totalExpenses = expenses,
                    balance = balance,
                    savingsRate = savingsRate,
                    transactionsCount = getTransactionsCount(),
                    averageExpense = getAverageExpense(),
                    averageIncome = getAverageIncome(),
                    expensesByCategory = realMap,
                    topExpenseInsight = insight,
                    isLoading = false,
                    error = null
                )
            }
        }
    }
}