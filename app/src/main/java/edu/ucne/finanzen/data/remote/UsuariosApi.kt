package edu.ucne.finanzen.data.remote

import edu.ucne.finanzen.data.remote.dto.UsuarioResponse
import retrofit2.Response
import retrofit2.http.*

interface UsuariosApi {

    @GET("api/Usuarios")
    suspend fun getUsuarios(): Response<List<UsuarioResponse>>

    @POST("api/Usuarios")
    suspend fun postUsuario(@Body usuario: UsuarioResponse): Response<UsuarioResponse>

    @GET("api/Usuarios/{id}")
    suspend fun getUsuario(@Path("id") id: Int): Response<UsuarioResponse>

    @PUT("api/Usuarios/{id}")
    suspend fun putUsuario(
        @Path("id") id: Int,
        @Body usuario: UsuarioResponse): Response<Unit>
}