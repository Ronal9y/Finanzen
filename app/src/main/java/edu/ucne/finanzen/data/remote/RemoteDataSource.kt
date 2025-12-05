package edu.ucne.finanzen.data.remote

import edu.ucne.finanzen.data.remote.dto.*
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: UsuariosApi
) {
    // Constantes para mensajes de error consistentes
    companion object {
        private const val EMPTY_RESPONSE_ERROR = "Respuesta vacía del servidor"
        private const val NETWORK_ERROR = "Error de red"
        private const val UNEXPECTED_ERROR = "Error inesperado"
    }

    // Funcion generica para manejar todas las llamadas API
    private suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<T>
    ): Resource<T> {
        return try {
            val response = apiCall()
            handleResponse(response)
        } catch (e: IOException) {
            Resource.Error("$NETWORK_ERROR: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("$UNEXPECTED_ERROR: ${e.message}")
        }
    }

    // Funcion generica para manejar operaciones que no devuelven datos
    private suspend fun safeApiCallUnit(
        apiCall: suspend () -> Response<Unit>
    ): Resource<Unit> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("$NETWORK_ERROR: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("$UNEXPECTED_ERROR: ${e.message}")
        }
    }

    private fun <T> handleResponse(response: Response<T>): Resource<T> {
        return if (response.isSuccessful) {
            response.body()?.let { Resource.Success(it) }
                ?: Resource.Error(EMPTY_RESPONSE_ERROR)
        } else {
            Resource.Error("HTTP ${response.code()} ${response.message()}")
        }
    }

    // Usuarios
    suspend fun getUsuarios(): Resource<List<UsuarioResponse>> =
        safeApiCall { api.getUsuarios() }

    suspend fun getUsuario(id: Int): Resource<UsuarioResponse> =
        safeApiCall { api.getUsuario(id) }

    suspend fun postUsuario(request: UsuarioResponse): Resource<UsuarioResponse> {
        return safeApiCall {
            val dto = UsuarioRequest(
                userName = request.userName,
                password = request.password
            )
            api.postUsuario(dto)
        }
    }

    suspend fun putUsuario(id: Int, request: UsuarioResponse): Resource<Unit> {
        return safeApiCallUnit {
            val dto = UsuarioRequest(
                userName = request.userName,
                password = request.password
            )
            api.putUsuario(id, dto)
        }
    }

    suspend fun deleteUsuario(id: Int): Resource<Unit> =
        safeApiCallUnit { api.deleteUsuario(id) }

    // Budgets
    suspend fun getBudgets(): Resource<List<BudgetResponse>> =
        safeApiCall { api.getBudgets() }

    suspend fun getBudget(id: Int): Resource<BudgetResponse> =
        safeApiCall { api.getBudget(id) }

    suspend fun postBudget(request: BudgetRequest): Resource<BudgetResponse> =
        safeApiCall { api.postBudget(request) }

    suspend fun putBudget(id: Int, request: BudgetRequest): Resource<Unit> =
        safeApiCallUnit { api.putBudget(id, request) }

    suspend fun deleteBudget(id: Int): Resource<Unit> =
        safeApiCallUnit { api.deleteBudget(id) }

    // Debts
    suspend fun getDebts(): Resource<List<DebtResponse>> =
        safeApiCall { api.getDebts() }

    suspend fun getDebt(id: Int): Resource<DebtResponse> =
        safeApiCall { api.getDebt(id) }

    suspend fun postDebt(request: DebtRequest): Resource<DebtResponse> =
        safeApiCall { api.postDebt(request) }

    suspend fun putDebt(id: Int, request: DebtRequest): Resource<Unit> =
        safeApiCallUnit { api.putDebt(id, request) }

    suspend fun deleteDebt(id: Int): Resource<Unit> =
        safeApiCallUnit { api.deleteDebt(id) }

    // Goals
    suspend fun getGoals(): Resource<List<GoalResponse>> =
        safeApiCall { api.getGoals() }

    suspend fun getGoal(id: Int): Resource<GoalResponse> =
        safeApiCall { api.getGoal(id) }

    suspend fun postGoal(request: GoalRequest): Resource<GoalResponse> =
        safeApiCall { api.postGoal(request) }

    suspend fun putGoal(id: Int, request: GoalRequest): Resource<Unit> =
        safeApiCallUnit { api.putGoal(id, request) }

    suspend fun deleteGoal(id: Int): Resource<Unit> =
        safeApiCallUnit { api.deleteGoal(id) }

    // Transactions
    suspend fun getTransactions(): Resource<List<TransactionResponse>> =
        safeApiCall { api.getTransactions() }

    suspend fun getTransaction(id: Int): Resource<TransactionResponse> =
        safeApiCall { api.getTransaction(id) }

    suspend fun postTransaction(request: TransactionRequest): Resource<TransactionResponse> =
        safeApiCall { api.postTransaction(request) }

    suspend fun putTransaction(id: Int, request: TransactionRequest): Resource<Unit> =
        safeApiCallUnit { api.putTransaction(id, request) }

    suspend fun deleteTransaction(id: Int): Resource<Unit> =
        safeApiCallUnit { api.deleteTransaction(id) }
}