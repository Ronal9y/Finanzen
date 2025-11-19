package edu.ucne.finanzen.domain.repository

import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.data.remote.dto.UsuarioResponse

interface UsuarioRepository {

    suspend fun getUsuarios(): Resource<List<UsuarioResponse>>

    suspend fun getUsuario(id: Int): Resource<UsuarioResponse>

    suspend fun postUsuario(usuario: UsuarioResponse): Resource<UsuarioResponse>

    suspend fun putUsuario(id: Int, usuario: UsuarioResponse): Resource<Unit>
}