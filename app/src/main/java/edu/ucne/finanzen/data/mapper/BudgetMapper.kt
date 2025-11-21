package edu.ucne.finanzen.data.mapper

import edu.ucne.finanzen.data.local.entity.BudgetEntity
import edu.ucne.finanzen.domain.model.Budget

fun BudgetEntity.asExternalModel(): Budget = Budget(
    budgetId = budgetId,
    category = enumValueOf(category),
    limit = limit,
    spent = spent,
    month = month,
    alertThreshold = alertThreshold
)

fun Budget.toEntity(): BudgetEntity = BudgetEntity(
    budgetId = budgetId,
    category = category.name,
    limit = limit,
    spent = spent,
    month = month,
    alertThreshold = alertThreshold
)