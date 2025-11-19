package edu.ucne.finanzen.data.remote

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
            val response = api.postUsuario(request)
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
            val response = api.putUsuario(id, request)
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