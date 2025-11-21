package edu.ucne.finanzen.data.mapper

import edu.ucne.finanzen.data.local.entity.GoalEntity
import edu.ucne.finanzen.domain.model.Goal

fun GoalEntity.asExternalModel(): Goal = Goal(
    goalId = goalId,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline,
    description = description
)

fun Goal.toEntity(): GoalEntity = GoalEntity(
    goalId = goalId,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline,
    description = description
)