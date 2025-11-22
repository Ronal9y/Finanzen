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
import retrofit2.Response
import retrofit2.http.*

interface UsuariosApi {

    @GET("api/Usuarios")
    suspend fun getUsuarios(): Response<List<UsuarioResponse>>

    @POST("api/Usuarios")
    suspend fun postUsuario(@Body usuario: UsuarioRequest): Response<UsuarioResponse>

    @GET("api/Usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): Response<UsuarioResponse>

    @PUT("api/Usuarios/{id}")
    suspend fun putUsuario(@Path("id") id: Int, @Body usuario: UsuarioRequest): Response<Unit>

    @DELETE("api/Usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Unit>


    @GET("api/Budgets")
    suspend fun getBudgets(): Response<List<BudgetResponse>>

    @POST("api/Budgets")
    suspend fun postBudget(@Body budget: BudgetRequest): Response<BudgetResponse>

    @GET("api/Budgets/{id}")
    suspend fun getBudget(@Path("id") id: Int): Response<BudgetResponse>

    @PUT("api/Budgets/{id}")
    suspend fun putBudget(@Path("id") id: Int, @Body budget: BudgetRequest): Response<Unit>

    @DELETE("api/Budgets/{id}")
    suspend fun deleteBudget(@Path("id") id: Int): Response<Unit>


    @GET("api/Debts")
    suspend fun getDebts(): Response<List<DebtResponse>>

    @POST("api/Debts")
    suspend fun postDebt(@Body debt: DebtRequest): Response<DebtResponse>

    @GET("api/Debts/{id}")
    suspend fun getDebt(@Path("id") id: Int): Response<DebtResponse>

    @PUT("api/Debts/{id}")
    suspend fun putDebt(@Path("id") id: Int, @Body debt: DebtRequest): Response<Unit>

    @DELETE("api/Debts/{id}")
    suspend fun deleteDebt(@Path("id") id: Int): Response<Unit>


    @GET("api/Goals")
    suspend fun getGoals(): Response<List<GoalResponse>>

    @POST("api/Goals")
    suspend fun postGoal(@Body goal: GoalRequest): Response<GoalResponse>

    @GET("api/Goals/{id}")
    suspend fun getGoal(@Path("id") id: Int): Response<GoalResponse>

    @PUT("api/Goals/{id}")
    suspend fun putGoal(@Path("id") id: Int, @Body goal: GoalRequest): Response<Unit>

    @DELETE("api/Goals/{id}")
    suspend fun deleteGoal(@Path("id") id: Int): Response<Unit>


    @GET("api/Transactions")
    suspend fun getTransactions(): Response<List<TransactionResponse>>

    @POST("api/Transactions")
    suspend fun postTransaction(@Body transaction: TransactionRequest): Response<TransactionResponse>

    @GET("api/Transactions/{id}")
    suspend fun getTransaction(@Path("id") id: Int): Response<TransactionResponse>

    @PUT("api/Transactions/{id}")
    suspend fun putTransaction(@Path("id") id: Int, @Body transaction: TransactionRequest): Response<Unit>

    @DELETE("api/Transactions/{id}")
    suspend fun deleteTransaction(@Path("id") id: Int): Response<Unit>
}