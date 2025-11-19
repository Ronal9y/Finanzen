package edu.ucne.finanzen.domain.usecases

import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.data.remote.dto.UsuarioResponse
import edu.ucne.finanzen.domain.repository.UsuarioRepository
import javax.inject.Inject

class ObtenerUsuariosUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(): Resource<List<UsuarioResponse>> {
        return repository.getUsuarios()
    }
}