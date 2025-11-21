package edu.ucne.finanzen.domain.usecases.Login

import edu.ucne.finanzen.domain.usecases.Login.ValidarUsuariouseCase
import javax.inject.Inject

data class UsuariosUseCase @Inject constructor(
    val validarUsuario: ValidarUsuariouseCase,
    val guardarUsuario: GuardarUsuariouseCase,
    val obtenerUsuario: ObtenerUsuarioUseCase,
    val obtenerUsuarios: ObtenerUsuariosUseCase
)