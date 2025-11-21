package edu.ucne.finanzen.presentation.goals

import edu.ucne.finanzen.domain.model.Goal

data class GoalsState(
    val goals: List<Goal> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
