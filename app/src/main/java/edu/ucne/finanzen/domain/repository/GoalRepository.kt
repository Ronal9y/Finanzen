package edu.ucne.finanzen.domain.repository

import edu.ucne.finanzen.domain.model.Goal
import kotlinx.coroutines.flow.Flow

interface GoalRepository {
    fun getAllGoals(): Flow<List<Goal>>
    suspend fun getGoalById(id: Int): Goal?
    suspend fun insertGoal(goal: Goal)
    suspend fun updateGoal(goal: Goal)
    suspend fun deleteGoal(goal: Goal)
    suspend fun deleteGoalById(id: Int)
    suspend fun getGoalsCount(): Int
    suspend fun getTotalCompletionPercentage(): Double
}