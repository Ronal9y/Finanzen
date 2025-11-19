package edu.ucne.finanzen.domain.usecases

import edu.ucne.finanzen.data.remote.dto.UsuarioResponse
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.domain.repository.UsuarioRepository
import javax.inject.Inject

class ValidarUsuariouseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(usuario: UsuarioResponse, currentId: Int? = null): Result<Unit> {
        if (usuario.userName.isNullOrBlank()) {
            return Result.failure(IllegalArgumentException("El nombre de usuario no puede estar vacío"))
        }

        if (usuario.password.isNullOrBlank()) {
            return Result.failure(IllegalArgumentException("La contraseña no puede estar vacía"))
        }

        val usuariosResource = repository.getUsuarios()

        val existentes = when (usuariosResource) {
            is Resource.Success -> usuariosResource.data ?: emptyList()
            is Resource.Error -> {
                return Result.failure(
                    IllegalStateException(
                        usuariosResource.message ?: "No se pudieron obtener los usuarios para validar"
                    )
                )
            }
            is Resource.Loading -> emptyList()
        }

        val userNameRepetido = existentes.any {
            it.userName.equals(usuario.userName, ignoreCase = true) &&
                    it.usuarioId != (currentId ?: it.usuarioId)
        }

        return if (userNameRepetido) {
            Result.failure(IllegalStateException("Ya existe un usuario con ese nombre"))
        } else {
            Result.success(Unit)
        }
    }
}