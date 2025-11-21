package edu.ucne.finanzen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import  edu.ucne.finanzen.domain.model.CategoryType

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val budgetId: Int = 0,
    val category: String,
    val limit: Double,
    val spent: Double = 0.0,
    val month: String,
    val alertThreshold: Int = 80
)
