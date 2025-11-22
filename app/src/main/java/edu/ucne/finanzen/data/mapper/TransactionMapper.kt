package edu.ucne.finanzen.data.mapper

import edu.ucne.finanzen.data.local.entity.TransactionEntity
import edu.ucne.finanzen.data.remote.dto.TransactionRequest
import edu.ucne.finanzen.data.remote.dto.TransactionResponse
import edu.ucne.finanzen.domain.model.CategoryType
import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.model.TransactionType

fun TransactionEntity.asExternalModel(): Transaction = Transaction(
    transactionId = transactionId,
    type = type,
    amount = amount,
    category = category,
    description = description,
    usuarioId = usuarioId,
    date = date
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    transactionId = transactionId,
    type = type,
    amount = amount,
    category = category,
    description = description,
    usuarioId = usuarioId,
    date = date
)

fun TransactionResponse.toDomain(): Transaction = Transaction(
    transactionId = transactionId,
    type = enumValueOf<TransactionType>(type),
    amount = amount,
    category = enumValueOf<CategoryType>(category),
    description = description,
    usuarioId = usuarioId,
    date = date
)

fun Transaction.toRequest(): TransactionRequest = TransactionRequest(
    type = type.name,
    amount = amount,
    category = category.name,
    description = description,
    date = date,
    usuarioId = usuarioId
)