package edu.ucne.finanzen.presentation.debts

import edu.ucne.finanzen.domain.model.Debt

sealed interface DeudaEvent {
    object Refrescar : DeudaEvent
    data class GuardarDeuda(val deuda: Debt) : DeudaEvent
    data class EliminarDeuda(val id: Int) : DeudaEvent
    data class PagarCuota(val id: Int, val monto: Double) : DeudaEvent
    data class MostrarDialogoPago(val id: Int?) : DeudaEvent
    data class RenovarDeuda(
        val id: Int,
        val nuevaFecha: String,
        val penalizacionAdicional: Double = 0.0
    ) : DeudaEvent
    object MostrarDialogoAgregar : DeudaEvent
    object CerrarDialogoAgregar : DeudaEvent
    object CerrarDialogoRenovar : DeudaEvent
    data class MostrarDialogoRenovar(val id: Int) : DeudaEvent

}