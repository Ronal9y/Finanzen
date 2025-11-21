package edu.ucne.finanzen.presentation.budgets

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.finanzen.common.NotificationHelper
import edu.ucne.finanzen.domain.model.Budget
import edu.ucne.finanzen.domain.usecases.Budgets.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetsViewModel @Inject constructor(
    private val getBudgets: GetBudgetsUseCase,
    private val deleteBudgetById: DeleteBudgetByIdUseCase,
    private val insertBudget: InsertBudgetUseCase,
    private val updateBudgetSpent: UpdateBudgetSpentUseCase,
    @ApplicationContext private val context: Context
) : AndroidViewModel(context as Application) {

    private val _state = MutableStateFlow(BudgetsState())
    val state: StateFlow<BudgetsState> = _state.asStateFlow()

    init {
        loadBudgets()
        syncSpent()
    }

    fun onEvent(event: BudgetsEvent) {
        when (event) {
            BudgetsEvent.Refresh -> {
                syncSpent()
                loadBudgets()
            }
            is BudgetsEvent.DeleteBudget -> deleteBudget(event.id)
            is BudgetsEvent.AddBudget -> addBudget(event)
        }
    }

    private fun loadBudgets() = viewModelScope.launch {
        getBudgets().collect { budgets ->
            _state.update { it.copy(budgets = budgets, isLoading = false, error = null) }
        }
    }

    private fun syncSpent() = viewModelScope.launch {
        runCatching {
            updateBudgetSpent()
        }.onSuccess {
            loadBudgets()

            state.value.budgets.forEach { budget ->
                checkAndNotifyThreshold(budget)
            }
        }.onFailure {
            _state.update { s -> s.copy(error = it.message) }
        }
    }

    private fun deleteBudget(id: Int) = viewModelScope.launch {
        runCatching { deleteBudgetById(id) }
            .onFailure { _state.update { s -> s.copy(error = it.message) } }
    }

    private fun addBudget(event: BudgetsEvent.AddBudget) = viewModelScope.launch {
        val newBudget = Budget(
            budgetId = 0,
            category = event.category,
            limit = event.limit,
            spent = 0.0,
            month = "Mes actual",
            alertThreshold = event.alertThreshold
        )
        runCatching { insertBudget(newBudget) }
            .onFailure { _state.update { s -> s.copy(error = it.message) } }
    }
    private fun checkAndNotifyThreshold(budget: Budget) {
        val used = budget.spent / budget.limit
        val percentage = (used * 100).toInt()
        if (used >= budget.alertThreshold / 100.0) {
            NotificationHelper.showBudgetAlert(
                context = getApplication<Application>().applicationContext,
                category = budget.category.name,
                percentage = percentage
            )
        }
    }
}