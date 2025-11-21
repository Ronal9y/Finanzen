package edu.ucne.finanzen.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import edu.ucne.finanzen.data.remote.dto.UsuarioResponse
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.domain.usecases.Login.UsuariosUseCase

@HiltViewModel
open class LoginViewModel @Inject constructor(
    private val usuariosUseCase: UsuariosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    open val state: StateFlow<LoginState> = _state

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UserNameChanged -> {
                _state.value = _state.value.copy(
                    userNameInput = event.value,
                    error = null
                )
            }
            is LoginEvent.PasswordChanged -> {
                _state.value = _state.value.copy(
                    passwordInput = event.value,
                    error = null
                )
            }
            is LoginEvent.Save -> {
                saveUsuario()
            }
            is LoginEvent.Edit -> {
                _state.value = _state.value.copy(
                    isDialogOpen = true,
                    isEditing = true,
                    usuarioId = event.usuario.usuarioId,
                    userNameInput = event.usuario.userName.orEmpty(),
                    passwordInput = event.usuario.password.orEmpty(),
                    error = null
                )
            }
            is LoginEvent.New -> {
                _state.value = _state.value.copy(
                    isDialogOpen = true,
                    isEditing = false,
                    usuarioId = null,
                    error = null
                )
            }
            is LoginEvent.DismissDialog -> {
                _state.value = _state.value.copy(
                    isDialogOpen = false,
                    isEditing = false,
                    usuarioId = null,
                    error = null
                )
            }
            is LoginEvent.LoadUsuarios -> {
                loadUsuarios()
            }
            is LoginEvent.Login -> {
                login()
            }
            is LoginEvent.Logout -> {
                _state.value = _state.value.copy(
                    isLoggedIn = false,
                    loggedInUserName = null,
                    userNameInput = "",
                    passwordInput = "",
                    error = null
                )
            }
        }
    }

    private fun loadUsuarios() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = usuariosUseCase.obtenerUsuarios()) {
                is Resource.Success -> {
                    val usuariosList = result.data ?: emptyList()
                    _state.value = _state.value.copy(
                        usuarios = usuariosList,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    val msg = result.message ?: "Error al obtener usuarios"
                    _state.value = _state.value.copy(
                        usuarios = emptyList(),
                        isLoading = false,
                        error = msg
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    private fun saveUsuario() {
        val currentState = _state.value
        val usuarioId = currentState.usuarioId

        val usuario = UsuarioResponse(
            usuarioId = usuarioId ?: 0,
            userName = currentState.userNameInput,
            password = currentState.passwordInput
        )

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            val validacion = usuariosUseCase.validarUsuario(usuario, usuarioId)
            if (validacion.isFailure) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = validacion.exceptionOrNull()?.message ?: "Error de validación"
                )
                return@launch
            }

            when (val result = usuariosUseCase.guardarUsuario(usuarioId, usuario)) {
                is Resource.Success<*> -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        isDialogOpen = false,
                        isEditing = false,
                        usuarioId = null,
                        error = null,
                        isLoggedIn = true,
                        loggedInUserName = usuario.userName
                    )
                    loadUsuarios()
                }
                is Resource.Error<*> -> {
                    val msg = result.message ?: "Error al guardar usuario"
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = msg
                    )
                }
                is Resource.Loading<*> -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }

    private fun login() {
        val currentState = _state.value
        val userName = currentState.userNameInput
        val password = currentState.passwordInput

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = usuariosUseCase.obtenerUsuarios()) {
                is Resource.Success -> {
                    val usuariosList = result.data ?: emptyList()
                    val usuario = usuariosList.firstOrNull {
                        it.userName.equals(userName, ignoreCase = true) &&
                                it.password == password
                    }
                    if (usuario != null) {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            loggedInUserName = usuario.userName,
                            error = null
                        )
                    } else {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Usuario o contraseña incorrectos"
                        )
                    }
                }
                is Resource.Error -> {
                    val msg = result.message ?: "Error al iniciar sesión"
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = msg
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(
                        isLoading = true
                    )
                }
            }
        }
    }
}