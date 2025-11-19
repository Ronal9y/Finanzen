package edu.ucne.finanzen.domain.usecases

import edu.ucne.finanzen.data.remote.dto.UsuarioResponse
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.domain.repository.UsuarioRepository
import javax.inject.Inject

class GuardarUsuariouseCase @Inject constructor(
    private val repository: UsuarioRepository,
    private val validarUsuario: ValidarUsuariouseCase
) {
    suspend operator fun invoke(usuarioId: Int?, usuario: UsuarioResponse): Result<Unit> {
        val validacion = validarUsuario(usuario, usuarioId)
        if (validacion.isFailure) {
            return Result.failure(validacion.exceptionOrNull()!!)
        }

        return try {
            val response: Resource<*> = if (usuarioId == null || usuarioId == 0) {
                repository.postUsuario(
                    usuario.copy(usuarioId = 0)
                )
            } else {
                repository.putUsuario(
                    usuarioId,
                    usuario.copy(usuarioId = usuarioId)
                )
            }

            if (response.isSuccess()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Error al guardar el usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}