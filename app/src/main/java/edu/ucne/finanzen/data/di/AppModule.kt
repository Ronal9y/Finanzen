package edu.ucne.finanzen.data.di

import android.content.Context
import androidx.room.Room
import edu.ucne.finanzen.data.local.database.FinanceDatabase
import edu.ucne.finanzen.data.local.dao.*
import edu.ucne.finanzen.data.repository.*
import edu.ucne.finanzen.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFinanceDatabase(@ApplicationContext context: Context): FinanceDatabase {
        return Room.databaseBuilder(
            context,
            FinanceDatabase::class.java,
            "finance_db"
        )
            .fallbackToDestructiveMigration()
            //.addMigrations()
            .build()
    }

    // DAOs
    @Provides
    @Singleton
    fun provideTransactionDao(database: FinanceDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    @Singleton
    fun provideBudgetDao(database: FinanceDatabase): BudgetDao {
        return database.budgetDao()
    }

    @Provides
    @Singleton
    fun provideGoalDao(database: FinanceDatabase): GoalDao {
        return database.goalDao()
    }

    @Provides
    @Singleton
    fun provideDebtDao(database: FinanceDatabase): DebtDao {
        return database.debtDao()
    }

    // Repositories
    @Provides
    @Singleton
    fun provideTransactionRepository(dao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideBudgetRepository(dao: BudgetDao): BudgetRepository {
        return BudgetRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideGoalRepository(dao: GoalDao): GoalRepository {
        return GoalRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideDebtRepository(dao: DebtDao): DebtRepository {
        return DebtRepositoryImpl(dao)
    }

}