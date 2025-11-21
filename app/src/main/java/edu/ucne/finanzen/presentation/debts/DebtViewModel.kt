package edu.ucne.finanzen.presentation.debts

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.finanzen.domain.model.Debt
import edu.ucne.finanzen.domain.model.DebtStatus
import edu.ucne.finanzen.domain.repository.DebtRepository
import edu.ucne.finanzen.domain.usecases.Debts.GetDebtsWithInterestUseCase
import edu.ucne.finanzen.domain.utils.InterestCalculator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.finanzen.common.NotificationHelper

@HiltViewModel
class DebtViewModel @Inject constructor(
    private val getDebtsUC: GetDebtsWithInterestUseCase,
    private val repo: DebtRepository,
    @ApplicationContext private val context: Context
) : AndroidViewModel(context as Application) {

    private val _uiState = MutableStateFlow(DebtUiState())
    val uiState: StateFlow<DebtUiState> = _uiState.asStateFlow()

    init { cargar() }

    fun onEvent(event: DebtEvent) {
        when (event) {
            DebtEvent.MostrarDialogoAgregar -> {
                _uiState.update { it.copy(mostrarDialogoAgregar = true) }
            }
            DebtEvent.CerrarDialogoAgregar -> {
                _uiState.update { it.copy(mostrarDialogoAgregar = false) }
            }
            DebtEvent.Refrescar -> cargar()
            is DebtEvent.GuardarDeuda -> guardar(event.deuda)
            is DebtEvent.EliminarDeuda -> eliminar(event.id)
            is DebtEvent.PagarCuota -> pagar(event.id, event.monto)
            is DebtEvent.MostrarDialogoPago -> {
                _uiState.update { it.copy(mostrarDialogoPago = if (event.id == -1) null else event.id) }
            }
            is DebtEvent.RenovarDeuda -> renovarConFecha(event.id, event.nuevaFecha, event.penalizacionAdicional)
            is DebtEvent.MostrarDialogoRenovar -> {
                _uiState.update { it.copy(mostrarDialogoRenovar = event.id) }
            }
            DebtEvent.CerrarDialogoRenovar -> {
                _uiState.update { it.copy(mostrarDialogoRenovar = null) }
            }
        }
    }

    private fun renovarConFecha(id: Int, nuevaFecha: String, penalizacionAdicional: Double = 0.0) = viewModelScope.launch {
        val deuda = repo.getDebtById(id) ?: return@launch

        val saldoActual = deuda.remainingAmount

        val montoRenovado = InterestCalculator.calculateRenewalAmount(
            currentBalance = saldoActual,
            penaltyRate = deuda.penaltyRate,
            additionalPenalty = penalizacionAdicional
        )

        val deudaRenovada = deuda.copy(
            dueDate = nuevaFecha,
            remainingAmount = montoRenovado,
            status = DebtStatus.ACTIVE,
            creationDate = hoy()
        )

        repo.saveDebt(deudaRenovada)
        onEvent(DebtEvent.CerrarDialogoRenovar)
        cargar()
    }

    private fun cargar() = viewModelScope.launch {
        _uiState.update { it.copy(cargando = true, error = null) }

        repo.getAllDebts().collect { lista ->

            val deudasActualizadas = lista.map { deuda ->
                when {

                    deuda.status == DebtStatus.PAID -> deuda


                    deuda.remainingAmount == deuda.principalAmount -> {
                        val saldoActual = calcularSaldoActual(deuda)
                        val nuevoEstado = if (diasEntre(hoy(), deuda.dueDate) < 0) {
                            DebtStatus.OVERDUE
                        } else {
                            DebtStatus.ACTIVE
                        }
                        deuda.copy(
                            remainingAmount = saldoActual,
                            status = nuevoEstado
                        )
                    }

                    else -> {
                        val nuevoEstado = if (diasEntre(hoy(), deuda.dueDate) < 0) {
                            DebtStatus.OVERDUE
                        } else {
                            deuda.status
                        }
                        deuda.copy(status = nuevoEstado)
                    }
                }
            }

            val deudasActivas = deudasActualizadas.filter {
                it.status == DebtStatus.ACTIVE || it.status == DebtStatus.OVERDUE
            }
            val totalRestante = deudasActivas.sumOf { it.remainingAmount }

            _uiState.update {
                it.copy(
                    deudas = deudasActualizadas,
                    cantidadActivas = deudasActivas.size,
                    totalRestante = totalRestante,
                    cargando = false
                )
            }
            checkAndNotifyDueDebts(deudasActualizadas)

        }
    }

    private fun guardar(deuda: Debt) = viewModelScope.launch {
        val deudaParaGuardar = deuda.copy(
            remainingAmount = deuda.principalAmount,
            status = DebtStatus.ACTIVE,
            creationDate = hoy()
        )
        repo.saveDebt(deudaParaGuardar)
    }

    private fun eliminar(id: Int) = viewModelScope.launch {
        repo.deleteDebtById(id)
    }

    private fun pagar(id: Int, monto: Double) = viewModelScope.launch {
        val deuda = repo.getDebtById(id) ?: return@launch

        val nuevoRemaining = (deuda.remainingAmount - monto).coerceAtLeast(0.0)

        val deudaActualizada = deuda.copy(
            remainingAmount = nuevoRemaining,
            status = if (nuevoRemaining <= 0.0) DebtStatus.PAID else deuda.status
        )

        repo.saveDebt(deudaActualizada)
        cargar()
    }

    private fun calcularSaldoActual(deuda: Debt): Double {
        val isOverdue = diasEntre(hoy(), deuda.dueDate) < 0
        return InterestCalculator.calculateCurrentBalance(
            principal = deuda.principalAmount,
            interestRate = deuda.interestRate,
            interestType = deuda.interestType,
            compoundingPeriod = deuda.compoundingPeriod,
            creationDate = deuda.creationDate,
            penaltyRate = if (isOverdue) deuda.penaltyRate else 0.0,
            isOverdue = isOverdue
        )
    }

    private fun hoy(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun diasEntre(start: String, end: String): Long {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = fmt.parse(start) ?: return 0
        val endDate = fmt.parse(end) ?: return 0
        val ms = endDate.time - startDate.time
        return TimeUnit.DAYS.convert(ms, TimeUnit.MILLISECONDS)
    }
    private fun checkAndNotifyDueDebts(debts: List<Debt>) {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = fmt.format(Date()) // hoy como String

        debts.forEach { debt ->
            val dueDate = debt.dueDate
            val daysLeft = diasEntre(today, dueDate)
            if (daysLeft == 1L && debt.remainingAmount > 0) {
                NotificationHelper.showDebtDueSoonAlert(
                    context = getApplication<Application>().applicationContext,
                    debtName = debt.name,
                    daysLeft = daysLeft.toInt()
                )
            }
        }
    }
}