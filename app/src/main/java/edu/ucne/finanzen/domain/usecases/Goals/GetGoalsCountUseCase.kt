package edu.ucne.finanzen.domain.usecases.Goals

import edu.ucne.finanzen.domain.repository.GoalRepository
import javax.inject.Inject

class GetGoalsCountUseCase @Inject constructor(
    private val repository: GoalRepository
) {
    suspend operator fun invoke(usuarioId: Int): Int =
        repository.getGoalsCount(usuarioId)
}