package edu.ucne.finanzen.presentation.debts

import edu.ucne.finanzen.domain.model.Debt

sealed interface DebtEvent {
    object Refrescar : DebtEvent
    data class GuardarDeuda(val deuda: Debt) : DebtEvent
    data class EliminarDeuda(val id: Int) : DebtEvent
    data class PagarCuota(val id: Int, val monto: Double) : DebtEvent
    data class MostrarDialogoPago(val id: Int?) : DebtEvent
    data class RenovarDeuda(
        val id: Int,
        val nuevaFecha: String,
        val penalizacionAdicional: Double = 0.0
    ) : DebtEvent
    object MostrarDialogoAgregar : DebtEvent
    object CerrarDialogoAgregar : DebtEvent
    object CerrarDialogoRenovar : DebtEvent
    data class MostrarDialogoRenovar(val id: Int) : DebtEvent

}