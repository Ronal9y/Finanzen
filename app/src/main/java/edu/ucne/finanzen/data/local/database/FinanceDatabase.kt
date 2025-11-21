package edu.ucne.finanzen.data.local.database

import edu.ucne.finanzen.data.local.dao.BudgetDao
import edu.ucne.finanzen.data.local.dao.DebtDao
import edu.ucne.finanzen.data.local.dao.GoalDao
import edu.ucne.finanzen.data.local.dao.TransactionDao
import edu.ucne.finanzen.data.local.entity.BudgetEntity
import edu.ucne.finanzen.data.local.entity.DebtEntity
import edu.ucne.finanzen.data.local.entity.GoalEntity
import edu.ucne.finanzen.data.local.entity.TransactionEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        TransactionEntity::class,
        BudgetEntity::class,
        GoalEntity::class,
        DebtEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun goalDao(): GoalDao
    abstract fun debtDao(): DebtDao

}