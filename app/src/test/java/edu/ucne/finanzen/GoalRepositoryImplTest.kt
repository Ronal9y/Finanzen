package edu.ucne.finanzen.data.repository

import edu.ucne.finanzen.data.local.dao.GoalDao
import edu.ucne.finanzen.data.mapper.asExternalModel
import edu.ucne.finanzen.data.mapper.toEntity
import edu.ucne.finanzen.data.mapper.toGoalRequest
import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.domain.model.Goal
import edu.ucne.finanzen.domain.repository.GoalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalDao: GoalDao,
    private val remoteDataSource: RemoteDataSource
) : GoalRepository {

    override fun getAllGoals(usuarioId: Int): Flow<List<Goal>> =
        goalDao.observeAll().map { list ->
            list.map { it.asExternalModel() }
                .filter { it.usuarioId == usuarioId }
        }

    override suspend fun getGoalById(id: Int): Goal? =
        goalDao.getById(id)?.asExternalModel()

    override suspend fun insertGoal(goal: Goal) {
        val result = remoteDataSource.postGoal(goal.toGoalRequest())
        when (result) {
            is Resource.Success -> goalDao.upsert(goal.toEntity())
            is Resource.Error -> goalDao.upsert(goal.toEntity())
            is Resource.Loading -> {}
        }
    }

    override suspend fun updateGoal(goal: Goal) {
        val result = remoteDataSource.putGoal(goal.goalId, goal.toGoalRequest())
        when (result) {
            is Resource.Success -> goalDao.upsert(goal.toEntity())
            is Resource.Error -> goalDao.upsert(goal.toEntity())
            is Resource.Loading -> {}
        }
    }

    override suspend fun deleteGoal(goal: Goal) {
        val result = remoteDataSource.deleteGoal(goal.goalId)
        when (result) {
            is Resource.Success -> goalDao.delete(goal.toEntity())
            is Resource.Error -> goalDao.delete(goal.toEntity())
            is Resource.Loading -> {}
        }
    }

    override suspend fun deleteGoalById(id: Int) {
        val result = remoteDataSource.deleteGoal(id)
        when (result) {
            is Resource.Success -> goalDao.deleteById(id)
            is Resource.Error -> goalDao.deleteById(id)
            is Resource.Loading -> {}
        }
    }

    override suspend fun getGoalsCount(usuarioId: Int): Int =
        goalDao.getAll().count { it.usuarioId == usuarioId }

    override suspend fun getTotalCompletionPercentage(usuarioId: Int): Double {
        val goals = goalDao.getAll()
            .map { it.asExternalModel() }
            .filter { it.usuarioId == usuarioId }
        return if (goals.isNotEmpty()) {
            goals.sumOf { it.currentAmount } / goals.sumOf { it.targetAmount } * 100
        } else 0.0
    }
}