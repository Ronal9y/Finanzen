package edu.ucne.finanzen.presentation.login

import edu.ucne.finanzen.data.remote.dto.UsuarioResponse

data class LoginState(
    val isLoading: Boolean = false,
    val usuarios: List<UsuarioResponse> = emptyList(),
    val error: String? = null,

    val isDialogOpen: Boolean = false,
    val isEditing: Boolean = false,
    val usuarioId: Int? = null,

    val userNameInput: String = "",
    val passwordInput: String = "",

    val isLoggedIn: Boolean = false,
    val loggedInUserName: String? = null
)