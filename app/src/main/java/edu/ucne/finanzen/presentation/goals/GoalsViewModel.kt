package edu.ucne.finanzen.presentation.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.finanzen.data.local.datastore.UserDataStore
import edu.ucne.finanzen.domain.model.Goal
import edu.ucne.finanzen.domain.usecases.Goals.DeleteGoalByIdUseCase
import edu.ucne.finanzen.domain.usecases.Goals.GetGoalsUseCase
import edu.ucne.finanzen.domain.usecases.Goals.InsertGoalUseCase
import edu.ucne.finanzen.domain.usecases.Goals.UpdateGoalUseCase
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val getGoals: GetGoalsUseCase,
    private val deleteGoalById: DeleteGoalByIdUseCase,
    private val updateGoal: UpdateGoalUseCase,
    private val insertGoal: InsertGoalUseCase,
    private val userDataStore: UserDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(GoalsState())
    val state: StateFlow<GoalsState> = _state.asStateFlow()

    private var goalsJob: Job? = null

    init {
        observeGoalsForUser()
    }

    fun onEvent(event: GoalsEvent) {
        when (event) {
            GoalsEvent.Refresh -> observeGoalsForUser()
            is GoalsEvent.DeleteGoal -> deleteGoal(event.id)
            is GoalsEvent.AddToGoal -> addToGoal(event.id, event.amount)
            is GoalsEvent.AddGoal -> addGoal(event)
        }
    }

    private fun observeGoalsForUser() {
        goalsJob?.cancel()
        goalsJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                val userId = userDataStore.userIdFlow.firstOrNull()
                if (userId != null) {
                    // Aquí se pasa el userId al use case
                    getGoals(userId).collect { goals ->
                        _state.update {
                            it.copy(
                                goals = goals,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                } else {
                    _state.update {
                        it.copy(
                            goals = emptyList(),
                            isLoading = false,
                            error = "Usuario no autenticado"
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar metas: ${e.message}"
                    )
                }
            }
        }
    }

    private fun deleteGoal(id: Int) = viewModelScope.launch {
        runCatching { deleteGoalById(id) }
            .onFailure { throwable ->
                _state.update { s -> s.copy(error = throwable.message) }
            }
    }

    private fun addToGoal(id: Int, amount: Double) = viewModelScope.launch {
        val goal = _state.value.goals.find { it.goalId == id } ?: return@launch
        val updated = goal.copy(currentAmount = goal.currentAmount + amount)
        runCatching { updateGoal(updated) }
            .onFailure { throwable ->
                _state.update { s -> s.copy(error = throwable.message) }
            }
    }

    private fun addGoal(event: GoalsEvent.AddGoal) = viewModelScope.launch {
        val userId = userDataStore.userIdFlow.firstOrNull() ?: return@launch

        val newGoal = Goal(
            name = event.name,
            targetAmount = event.targetAmount,
            currentAmount = 0.0,
            description = event.description,
            deadline = event.deadline,
            usuarioId = userId
        )

        runCatching { insertGoal(newGoal) }
            .onFailure { throwable ->
                _state.update { s -> s.copy(error = throwable.message) }
            }
    }
}