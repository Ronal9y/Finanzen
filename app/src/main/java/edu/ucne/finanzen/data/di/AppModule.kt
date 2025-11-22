package edu.ucne.finanzen.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.finanzen.data.local.dao.BudgetDao
import edu.ucne.finanzen.data.local.dao.DebtDao
import edu.ucne.finanzen.data.local.dao.GoalDao
import edu.ucne.finanzen.data.local.dao.TransactionDao
import edu.ucne.finanzen.data.local.database.FinanceDatabase
import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.UsuariosApi
import edu.ucne.finanzen.data.repository.BudgetRepositoryImpl
import edu.ucne.finanzen.data.repository.DebtRepositoryImpl
import edu.ucne.finanzen.data.repository.GoalRepositoryImpl
import edu.ucne.finanzen.data.repository.TransactionRepositoryImpl
import edu.ucne.finanzen.data.repository.UsuarioRepositoryImpl
import edu.ucne.finanzen.domain.repository.BudgetRepository
import edu.ucne.finanzen.domain.repository.DebtRepository
import edu.ucne.finanzen.domain.repository.GoalRepository
import edu.ucne.finanzen.domain.repository.TransactionRepository
import edu.ucne.finanzen.domain.repository.UsuarioRepository
import edu.ucne.finanzen.data.local.datastore.UserDataStore
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
            .build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(database: FinanceDatabase): TransactionDao = database.transactionDao()

    @Provides
    @Singleton
    fun provideBudgetDao(database: FinanceDatabase): BudgetDao = database.budgetDao()

    @Provides
    @Singleton
    fun provideGoalDao(database: FinanceDatabase): GoalDao = database.goalDao()

    @Provides
    @Singleton
    fun provideDebtDao(database: FinanceDatabase): DebtDao = database.debtDao()

    @Provides
    @Singleton
    fun provideRemoteDataSource(api: UsuariosApi): RemoteDataSource = RemoteDataSource(api)

    @Provides
    @Singleton
    fun provideUserDataStore(@ApplicationContext context: Context): UserDataStore {
        return UserDataStore(context)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        dao: TransactionDao,
        remoteDataSource: RemoteDataSource
    ): TransactionRepository = TransactionRepositoryImpl(dao, remoteDataSource)

    @Provides
    @Singleton
    fun provideBudgetRepository(
        dao: BudgetDao,
        remoteDataSource: RemoteDataSource
    ): BudgetRepository = BudgetRepositoryImpl(dao, remoteDataSource)

    @Provides
    @Singleton
    fun provideGoalRepository(
        dao: GoalDao,
        remoteDataSource: RemoteDataSource
    ): GoalRepository = GoalRepositoryImpl(dao, remoteDataSource)

    @Provides
    @Singleton
    fun provideDebtRepository(
        dao: DebtDao,
        remoteDataSource: RemoteDataSource
    ): DebtRepository = DebtRepositoryImpl(dao, remoteDataSource)

    @Provides
    @Singleton
    fun provideUsuarioRepository(
        remoteDataSource: RemoteDataSource
    ): UsuarioRepository = UsuarioRepositoryImpl(remoteDataSource)
}