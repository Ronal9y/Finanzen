package edu.ucne.finanzen.data.mapper

import edu.ucne.finanzen.data.local.entity.GoalEntity
import edu.ucne.finanzen.data.remote.dto.GoalRequest
import edu.ucne.finanzen.data.remote.dto.GoalResponse
import edu.ucne.finanzen.domain.model.Goal

fun GoalEntity.asExternalModel(): Goal = Goal(
    goalId = goalId,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline,
    usuarioId = this.usuarioId,
    description = description
)

fun Goal.toEntity(): GoalEntity = GoalEntity(
    goalId = goalId,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline,
    usuarioId = this.usuarioId,
    description = description
)

fun GoalResponse.toDomain(): Goal = Goal(
    goalId = goalId,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline,
    usuarioId = this.usuarioId,
    description = description
)

fun Goal.toGoalRequest(): GoalRequest = GoalRequest(
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline,
    usuarioId = this.usuarioId,
    description = description
)