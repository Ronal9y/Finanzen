package edu.ucne.finanzen.data.mapper

import edu.ucne.finanzen.data.remote.dto.UsuarioResponse
import edu.ucne.finanzen.domain.model.Usuario

fun UsuarioResponse.toDomain(): Usuario {
    return Usuario(
        usuarioId = this.usuarioId,
        userName = this.userName,
        password = this.password
    )
}

fun Usuario.toDto(): UsuarioResponse {
    return UsuarioResponse(
        usuarioId = this.usuarioId,
        userName = this.userName,
        password = this.password
    )
}