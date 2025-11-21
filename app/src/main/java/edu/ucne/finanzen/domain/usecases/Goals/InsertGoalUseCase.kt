package edu.ucne.finanzen.domain.usecases.Goals

import edu.ucne.finanzen.domain.model.Goal
import edu.ucne.finanzen.domain.repository.GoalRepository
import javax.inject.Inject

class InsertGoalUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    suspend operator fun invoke(goal: Goal) = repository.insertGoal(goal)
}