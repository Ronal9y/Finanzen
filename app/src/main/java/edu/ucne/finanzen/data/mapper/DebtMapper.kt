package edu.ucne.finanzen.data.mapper

import edu.ucne.finanzen.data.local.entity.DebtEntity
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
    creationDate = creationDate
)