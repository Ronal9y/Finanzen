package edu.ucne.finanzen.data.repository

import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.data.remote.dto.BudgetRequest
import edu.ucne.finanzen.data.remote.dto.BudgetResponse
import edu.ucne.finanzen.data.remote.dto.DebtRequest
import edu.ucne.finanzen.data.remote.dto.DebtResponse
import edu.ucne.finanzen.data.remote.dto.GoalRequest
import edu.ucne.finanzen.data.remote.dto.GoalResponse
import edu.ucne.finanzen.data.remote.dto.TransactionRequest
import edu.ucne.finanzen.data.remote.dto.TransactionResponse
import edu.ucne.finanzen.data.remote.dto.UsuarioResponse
import edu.ucne.finanzen.domain.repository.UsuarioRepository
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : UsuarioRepository {

    override suspend fun getUsuarios(): Resource<List<UsuarioResponse>> {
        return remoteDataSource.getUsuarios()
    }

    override suspend fun getUsuario(id: Int): Resource<UsuarioResponse> {
        return remoteDataSource.getUsuario(id)
    }

    override suspend fun postUsuario(usuario: UsuarioResponse): Resource<UsuarioResponse> {
        return remoteDataSource.postUsuario(usuario)
    }

    override suspend fun putUsuario(id: Int, usuario: UsuarioResponse): Resource<Unit> {
        return remoteDataSource.putUsuario(id, usuario)
    }

    suspend fun deleteUsuario(id: Int): Resource<Unit> {
        return remoteDataSource.deleteUsuario(id)
    }

    suspend fun getBudgets(): Resource<List<BudgetResponse>> {
        return remoteDataSource.getBudgets()
    }

    suspend fun getBudget(id: Int): Resource<BudgetResponse> {
        return remoteDataSource.getBudget(id)
    }

    suspend fun postBudget(request: BudgetRequest): Resource<BudgetResponse> {
        return remoteDataSource.postBudget(request)
    }

    suspend fun putBudget(id: Int, request: BudgetRequest): Resource<Unit> {
        return remoteDataSource.putBudget(id, request)
    }

    suspend fun deleteBudget(id: Int): Resource<Unit> {
        return remoteDataSource.deleteBudget(id)
    }

    suspend fun getDebts(): Resource<List<DebtResponse>> {
        return remoteDataSource.getDebts()
    }

    suspend fun getDebt(id: Int): Resource<DebtResponse> {
        return remoteDataSource.getDebt(id)
    }

    suspend fun postDebt(request: DebtRequest): Resource<DebtResponse> {
        return remoteDataSource.postDebt(request)
    }

    suspend fun putDebt(id: Int, request: DebtRequest): Resource<Unit> {
        return remoteDataSource.putDebt(id, request)
    }

    suspend fun deleteDebt(id: Int): Resource<Unit> {
        return remoteDataSource.deleteDebt(id)
    }

    suspend fun getGoals(): Resource<List<GoalResponse>> {
        return remoteDataSource.getGoals()
    }

    suspend fun getGoal(id: Int): Resource<GoalResponse> {
        return remoteDataSource.getGoal(id)
    }

    suspend fun postGoal(request: GoalRequest): Resource<GoalResponse> {
        return remoteDataSource.postGoal(request)
    }

    suspend fun putGoal(id: Int, request: GoalRequest): Resource<Unit> {
        return remoteDataSource.putGoal(id, request)
    }

    suspend fun deleteGoal(id: Int): Resource<Unit> {
        return remoteDataSource.deleteGoal(id)
    }

    suspend fun getTransactions(): Resource<List<TransactionResponse>> {
        return remoteDataSource.getTransactions()
    }

    suspend fun getTransaction(id: Int): Resource<TransactionResponse> {
        return remoteDataSource.getTransaction(id)
    }

    suspend fun postTransaction(request: TransactionRequest): Resource<TransactionResponse> {
        return remoteDataSource.postTransaction(request)
    }

    suspend fun putTransaction(id: Int, request: TransactionRequest): Resource<Unit> {
        return remoteDataSource.putTransaction(id, request)
    }

    suspend fun deleteTransaction(id: Int): Resource<Unit> {
        return remoteDataSource.deleteTransaction(id)
    }
}