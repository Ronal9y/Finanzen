package edu.ucne.finanzen.data.remote

import edu.ucne.finanzen.data.remote.dto.BudgetRequest
import edu.ucne.finanzen.data.remote.dto.BudgetResponse
import edu.ucne.finanzen.data.remote.dto.DebtRequest
import edu.ucne.finanzen.data.remote.dto.DebtResponse
import edu.ucne.finanzen.data.remote.dto.GoalRequest
import edu.ucne.finanzen.data.remote.dto.GoalResponse
import edu.ucne.finanzen.data.remote.dto.TransactionRequest
import edu.ucne.finanzen.data.remote.dto.TransactionResponse
import edu.ucne.finanzen.data.remote.dto.UsuarioRequest
import edu.ucne.finanzen.data.remote.dto.UsuarioResponse
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: UsuariosApi
) {
    suspend fun getUsuarios(): Resource<List<UsuarioResponse>> {
        return try {
            val response = api.getUsuarios()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getUsuario(id: Int): Resource<UsuarioResponse> {
        return try {
            val response = api.getUsuario(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun postUsuario(request: UsuarioResponse): Resource<UsuarioResponse> {
        return try {
            val dto = UsuarioRequest(
                userName = request.userName,
                password = request.password
            )
            val response = api.postUsuario(dto)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun putUsuario(id: Int, request: UsuarioResponse): Resource<Unit> {
        return try {
            val dto = UsuarioRequest(
                userName = request.userName,
                password = request.password
            )
            val response = api.putUsuario(id, dto)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deleteUsuario(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteUsuario(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getBudgets(): Resource<List<BudgetResponse>> {
        return try {
            val response = api.getBudgets()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getBudget(id: Int): Resource<BudgetResponse> {
        return try {
            val response = api.getBudget(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun postBudget(request: BudgetRequest): Resource<BudgetResponse> {
        return try {
            val response = api.postBudget(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun putBudget(id: Int, request: BudgetRequest): Resource<Unit> {
        return try {
            val response = api.putBudget(id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deleteBudget(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteBudget(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getDebts(): Resource<List<DebtResponse>> {
        return try {
            val response = api.getDebts()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getDebt(id: Int): Resource<DebtResponse> {
        return try {
            val response = api.getDebt(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun postDebt(request: DebtRequest): Resource<DebtResponse> {
        return try {
            val response = api.postDebt(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun putDebt(id: Int, request: DebtRequest): Resource<Unit> {
        return try {
            val response = api.putDebt(id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deleteDebt(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteDebt(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getGoals(): Resource<List<GoalResponse>> {
        return try {
            val response = api.getGoals()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getGoal(id: Int): Resource<GoalResponse> {
        return try {
            val response = api.getGoal(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun postGoal(request: GoalRequest): Resource<GoalResponse> {
        return try {
            val response = api.postGoal(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun putGoal(id: Int, request: GoalRequest): Resource<Unit> {
        return try {
            val response = api.putGoal(id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deleteGoal(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteGoal(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getTransactions(): Resource<List<TransactionResponse>> {
        return try {
            val response = api.getTransactions()
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun getTransaction(id: Int): Resource<TransactionResponse> {
        return try {
            val response = api.getTransaction(id)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun postTransaction(request: TransactionRequest): Resource<TransactionResponse> {
        return try {
            val response = api.postTransaction(request)
            if (response.isSuccessful) {
                response.body()?.let { Resource.Success(it) }
                    ?: Resource.Error("Respuesta vacía del servidor")
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun putTransaction(id: Int, request: TransactionRequest): Resource<Unit> {
        return try {
            val response = api.putTransaction(id, request)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }

    suspend fun deleteTransaction(id: Int): Resource<Unit> {
        return try {
            val response = api.deleteTransaction(id)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error("HTTP ${response.code()} ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Error de red: ${e.message}")
        } catch (e: HttpException) {
            Resource.Error("Error HTTP ${e.code()}: ${e.message()}")
        } catch (e: Exception) {
            Resource.Error("Error inesperado: ${e.message}")
        }
    }
}