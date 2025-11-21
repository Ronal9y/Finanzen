package edu.ucne.finanzen.data.repository

import edu.ucne.finanzen.data.local.dao.GoalDao
import edu.ucne.finanzen.data.mapper.asExternalModel
import edu.ucne.finanzen.data.mapper.toEntity
import edu.ucne.finanzen.domain.model.Goal
import edu.ucne.finanzen.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao
) : GoalRepository {

    override fun getAllGoals(): Flow<List<Goal>> =
        goalDao.observeAll().map { list -> list.map { it.asExternalModel() } }

    override suspend fun getGoalById(id: Int): Goal? =
        goalDao.getById(id)?.asExternalModel()

    override suspend fun insertGoal(goal: Goal) =
        goalDao.upsert(goal.toEntity())

    override suspend fun updateGoal(goal: Goal) =
        goalDao.upsert(goal.toEntity())

    override suspend fun deleteGoal(goal: Goal) =
        goalDao.delete(goal.toEntity())

    override suspend fun deleteGoalById(id: Int) =
        goalDao.deleteById(id)

    override suspend fun getGoalsCount(): Int =
        goalDao.getCount()

    override suspend fun getTotalCompletionPercentage(): Double {
        val goals = goalDao.getAll()
        return if (goals.isNotEmpty()) {
            goals.sumOf { it.currentAmount } / goals.sumOf { it.targetAmount } * 100
        } else 0.0
    }
}