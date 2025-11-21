package edu.ucne.finanzen.domain.usecases.Goals

import edu.ucne.finanzen.domain.model.Goal
import edu.ucne.finanzen.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGoalsUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    operator fun invoke(): Flow<List<Goal>> = repository.getAllGoals()
}