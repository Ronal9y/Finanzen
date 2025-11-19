package edu.ucne.finanzen.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioResponse(
    val usuarioId: Int,
    val userName: String? = null,
    val password: String? = null
)