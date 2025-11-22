package edu.ucne.finanzen.data.mapper

import edu.ucne.finanzen.data.local.entity.BudgetEntity
import edu.ucne.finanzen.data.remote.dto.BudgetRequest
import edu.ucne.finanzen.data.remote.dto.BudgetResponse
import edu.ucne.finanzen.domain.model.Budget

fun BudgetEntity.asExternalModel(): Budget = Budget(
    budgetId = budgetId,
    category = enumValueOf(category),
    limit = limit,
    spent = spent,
    month = month,
    usuarioId = this.usuarioId,
    alertThreshold = alertThreshold
)

fun Budget.toEntity(): BudgetEntity = BudgetEntity(
    budgetId = budgetId,
    category = category.name,
    limit = limit,
    spent = spent,
    month = month,
    usuarioId = this.usuarioId,
    alertThreshold = alertThreshold
)

fun BudgetResponse.toDomain(): Budget = Budget(
    budgetId = budgetId,
    category = enumValueOf(category),
    limit = limit,
    spent = spent,
    month = month,
    usuarioId = this.usuarioId,
    alertThreshold = alertThreshold
)

fun Budget.toBudgetRequest(): BudgetRequest = BudgetRequest(
    category = category.name,
    limit = limit,
    spent = spent,
    month = month,
    usuarioId = this.usuarioId,
    alertThreshold = alertThreshold
)