package edu.ucne.finanzen.presentation.goals

sealed interface GoalsEvent {
    data object Refresh : GoalsEvent
    data class DeleteGoal(val id: Int) : GoalsEvent
    data class AddToGoal(val id: Int, val amount: Double) : GoalsEvent
    data class AddGoal(
        val name: String,
        val targetAmount: Double,
        val description: String,
        val deadline: String
    ) : GoalsEvent
}