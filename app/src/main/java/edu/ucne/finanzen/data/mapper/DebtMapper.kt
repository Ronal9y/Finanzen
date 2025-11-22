package edu.ucne.finanzen.data.mapper

import edu.ucne.finanzen.data.local.entity.DebtEntity
import edu.ucne.finanzen.data.remote.dto.DebtRequest
import edu.ucne.finanzen.data.remote.dto.DebtResponse
import edu.ucne.finanzen.domain.model.Debt

fun DebtEntity.asExternalModel(): Debt = Debt(
    debtId = debtId,
    name = name,
    principalAmount = principalAmount,
    interestRate = interestRate,
    interestType = enumValueOf(interestType),
    compoundingPeriod = enumValueOf(compoundingPeriod),
    dueDate = dueDate,
    remainingAmount = remainingAmount,
    creditor = creditor,
    status = enumValueOf(status),
    penaltyRate = penaltyRate,
    usuarioId = this.usuarioId,
    creationDate = creationDate
)

fun Debt.toEntity(): DebtEntity = DebtEntity(
    debtId = debtId,
    name = name,
    principalAmount = principalAmount,
    interestRate = interestRate,
    interestType = interestType.name,
    compoundingPeriod = compoundingPeriod.name,
    dueDate = dueDate,
    remainingAmount = remainingAmount,
    creditor = creditor,
    status = status.name,
    penaltyRate = penaltyRate,
    usuarioId = this.usuarioId,
    creationDate = creationDate
)

fun DebtResponse.toDomain(): Debt = Debt(
    debtId = debtId,
    name = name,
    principalAmount = principalAmount,
    interestRate = interestRate,
    interestType = enumValueOf(interestType),
    compoundingPeriod = enumValueOf(compoundingPeriod),
    dueDate = dueDate,
    remainingAmount = remainingAmount,
    creditor = creditor,
    status = enumValueOf(status),
    penaltyRate = penaltyRate,
    usuarioId = this.usuarioId,
    creationDate = creationDate
)

fun Debt.toDebtRequest(): DebtRequest = DebtRequest(
    name = name,
    principalAmount = principalAmount,
    interestRate = interestRate,
    interestType = interestType.name,
    compoundingPeriod = compoundingPeriod.name,
    dueDate = dueDate,
    remainingAmount = remainingAmount,
    creditor = creditor,
    status = status.name,
    penaltyRate = penaltyRate,
    usuarioId = this.usuarioId
)