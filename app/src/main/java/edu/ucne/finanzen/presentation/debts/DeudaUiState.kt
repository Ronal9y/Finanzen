package edu.ucne.finanzen.presentation.debts

import edu.ucne.finanzen.domain.model.Debt

data class DeudaUiState(
    val deudas: List<Debt> = emptyList(),
    val cantidadActivas: Int = 0,
    val totalRestante: Double = 0.0,
    val cargando: Boolean = false,
    val error: String? = null,
    val mostrarDialogoPago: Int? = null,
    val mostrarDialogoAgregar: Boolean = false,
    val mostrarDialogoRenovar: Int? = null
)

