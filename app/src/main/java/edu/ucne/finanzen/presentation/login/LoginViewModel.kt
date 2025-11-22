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
import edu.ucne.finanzen.data.local.datastore.UserDataStore

@HiltViewModel
open class LoginViewModel @Inject constructor(
    private val usuariosUseCase: UsuariosUseCase,
    private val userDataStore: UserDataStore
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
            is LoginEvent.Save -> { saveUsuario() }
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
            is LoginEvent.LoadUsuarios -> { loadUsuarios() }
            is LoginEvent.Login -> { login() }
            is LoginEvent.Logout -> {
                viewModelScope.launch {
                    userDataStore.clearUserId()
                }
                _state.value = _state.value.copy(
                    isLoggedIn = false,
                    loggedInUserName = null,
                    loggedInUserId = null,
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
                    _state.value = _state.value.copy(
                        usuarios = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        usuarios = emptyList(),
                        isLoading = false,
                        error = result.message ?: "Error al obtener usuarios"
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
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
            _state.value = _state.value.copy(isLoading = true, error = null)

            val validacion = usuariosUseCase.validarUsuario(usuario, usuarioId)
            if (validacion.isFailure) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = validacion.exceptionOrNull()?.message ?: "Error de validación"
                )
                return@launch
            }

            when (usuariosUseCase.guardarUsuario(usuarioId, usuario)) {
                is Resource.Success<*> -> {
                    val usuariosResult = usuariosUseCase.obtenerUsuarios()
                    if (usuariosResult is Resource.Success) {
                        val usuariosList = usuariosResult.data ?: emptyList()
                        val creado = usuariosList.firstOrNull {
                            it.userName.equals(usuario.userName, ignoreCase = true) &&
                                    it.password == usuario.password
                        }

                        if (creado != null) {
                            userDataStore.saveUserId(creado.usuarioId)

                            _state.value = _state.value.copy(
                                isLoading = false,
                                isDialogOpen = false,
                                isEditing = false,
                                usuarioId = null,
                                isLoggedIn = true,
                                loggedInUserName = creado.userName,
                                loggedInUserId = creado.usuarioId
                            )
                        } else {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = "Usuario guardado, pero no se pudo recuperar"
                            )
                        }
                    }
                }
                is Resource.Error<*> -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Error al guardar usuario"
                    )
                }
                is Resource.Loading<*> -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    private fun login() {
        val userName = _state.value.userNameInput
        val password = _state.value.passwordInput

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

            when (val result = usuariosUseCase.obtenerUsuarios()) {
                is Resource.Success -> {
                    val usuario = result.data?.firstOrNull {
                        it.userName.equals(userName, ignoreCase = true) &&
                                it.password == password
                    }

                    if (usuario != null) {
                        userDataStore.saveUserId(usuario.usuarioId)

                        _state.value = _state.value.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            loggedInUserName = usuario.userName,
                            loggedInUserId = usuario.usuarioId
                        )
                    } else {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = "Usuario o contraseña incorrectos"
                        )
                    }
                }
                is Resource.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = result.message ?: "Error al iniciar sesión"
                    )
                }
                is Resource.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }
}