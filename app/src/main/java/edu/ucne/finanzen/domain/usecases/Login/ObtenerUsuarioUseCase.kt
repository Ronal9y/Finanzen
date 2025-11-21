package edu.ucne.finanzen.domain.usecases.Login

import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.data.remote.dto.UsuarioResponse
import edu.ucne.finanzen.domain.repository.UsuarioRepository
import javax.inject.Inject

class ObtenerUsuarioUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(id: Int): Resource<UsuarioResponse> {
        return repository.getUsuario(id)
    }
}