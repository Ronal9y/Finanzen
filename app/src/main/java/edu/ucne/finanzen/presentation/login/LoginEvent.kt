package edu.ucne.finanzen.presentation.login

import edu.ucne.finanzen.data.remote.dto.UsuarioResponse

sealed interface LoginEvent {
    data class UserNameChanged(val value: String) : LoginEvent
    data class PasswordChanged(val value: String) : LoginEvent
    object Save : LoginEvent
    data class Edit(val usuario: UsuarioResponse) : LoginEvent
    object New : LoginEvent
    object DismissDialog : LoginEvent
    object LoadUsuarios : LoginEvent
    object Login : LoginEvent
    object Logout : LoginEvent
}