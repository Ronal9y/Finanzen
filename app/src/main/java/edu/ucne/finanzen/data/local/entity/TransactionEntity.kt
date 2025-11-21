package edu.ucne.finanzen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import  edu.ucne.finanzen.domain.model.CategoryType
import  edu.ucne.finanzen.domain.model.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Int = 0,
    val type: TransactionType,
    val amount: Double,
    val category: CategoryType,
    val description: String,
    val date: String
)

