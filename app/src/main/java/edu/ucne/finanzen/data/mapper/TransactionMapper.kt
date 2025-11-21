package edu.ucne.finanzen.data.mapper

import edu.ucne.finanzen.data.local.entity.TransactionEntity
import edu.ucne.finanzen.domain.model.Transaction

fun TransactionEntity.asExternalModel(): Transaction = Transaction(
    transactionId = transactionId,
    type = type,
    amount = amount,
    category = category,
    description = description,
    date = date
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    transactionId = transactionId,
    type = type,
    amount = amount,
    category = category,
    description = description,
    date = date
)
