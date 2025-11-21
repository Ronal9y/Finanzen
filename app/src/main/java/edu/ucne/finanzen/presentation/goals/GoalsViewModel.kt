package edu.ucne.finanzen.presentation.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.finanzen.domain.model.Goal
import edu.ucne.finanzen.domain.usecases.Goals.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val getGoals: GetGoalsUseCase,
    private val deleteGoalById: DeleteGoalByIdUseCase,
    private val updateGoal: UpdateGoalUseCase,
    private val insertGoal: InsertGoalUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GoalsState())
    val state: StateFlow<GoalsState> = _state.asStateFlow()

    init { loadGoals() }

    fun onEvent(event: GoalsEvent) {
        when (event) {
            GoalsEvent.Refresh -> loadGoals()
            is GoalsEvent.DeleteGoal -> deleteGoal(event.id)
            is GoalsEvent.AddToGoal -> addToGoal(event.id, event.amount)
            is GoalsEvent.AddGoal -> addGoal(event)
        }
    }

    private fun loadGoals() = viewModelScope.launch {
        getGoals().collect { goals ->
            _state.update { it.copy(goals = goals, isLoading = false, error = null) }
        }
    }

    private fun deleteGoal(id: Int) = viewModelScope.launch {
        runCatching { deleteGoalById(id) }
            .onFailure { _state.update { s -> s.copy(error = it.message) } }
    }

    private fun addToGoal(id: Int, amount: Double) = viewModelScope.launch {
        val goal = _state.value.goals.find { it.goalId == id } ?: return@launch
        val updated = goal.copy(currentAmount = goal.currentAmount + amount)
        runCatching { updateGoal(updated) }
            .onFailure { _state.update { s -> s.copy(error = it.message) } }
    }

    private fun addGoal(event: GoalsEvent.AddGoal) = viewModelScope.launch {
        val newGoal = Goal(
            name = event.name,
            targetAmount = event.targetAmount,
            currentAmount = 0.0,
            description = event.description,
            deadline = event.deadline
        )
        runCatching { insertGoal(newGoal) }
            .onFailure { _state.update { s -> s.copy(error = it.message) } }
    }
}