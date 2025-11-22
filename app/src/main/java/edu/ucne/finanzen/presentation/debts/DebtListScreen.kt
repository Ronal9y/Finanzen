package edu.ucne.finanzen.presentation.debts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.finanzen.domain.model.CompoundingPeriod
import edu.ucne.finanzen.domain.model.Debt
import edu.ucne.finanzen.domain.model.DebtStatus
import edu.ucne.finanzen.domain.model.InterestType
import edu.ucne.finanzen.ui.theme.FinanzenTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtListScreen(
    onBack: () -> Unit,
    viewModel: DebtViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var montoPago by remember { mutableStateOf("") }
    var nuevaFechaRenovar by remember { mutableStateOf("") }

    if (state.mostrarDialogoPago != null) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(DebtEvent.MostrarDialogoPago(null)) },
            title = { Text("Abonar a deuda") },
            text = {
                Column {
                    Text("Monto a abonar:")
                    OutlinedTextField(
                        value = montoPago,
                        onValueChange = { montoPago = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("0.00") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val monto = montoPago.toDoubleOrNull() ?: 0.0
                        if (monto > 0) {
                            viewModel.onEvent(
                                DebtEvent.PagarCuota(state.mostrarDialogoPago!!, monto)
                            )
                            viewModel.onEvent(DebtEvent.MostrarDialogoPago(null))
                            montoPago = ""
                        }
                    }
                ) {
                    Text("Pagar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(DebtEvent.MostrarDialogoPago(null)) }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (state.mostrarDialogoAgregar) {
        DebtAddBottomSheet(
            onDismiss = { viewModel.onEvent(DebtEvent.CerrarDialogoAgregar) },
            onConfirm = { deuda ->
                viewModel.onEvent(DebtEvent.GuardarDeuda(deuda))
                viewModel.onEvent(DebtEvent.CerrarDialogoAgregar)
            }
        )
    }

    if (state.mostrarDialogoRenovar != null) {
        val deuda = state.deudas.find { it.debtId == state.mostrarDialogoRenovar }
        var penalizacionAdicional by remember { mutableStateOf("0.0") }

        AlertDialog(
            onDismissRequest = { viewModel.onEvent(DebtEvent.CerrarDialogoRenovar) },
            title = { Text("Renovar Deuda") },
            text = {
                Column {
                    deuda?.let {
                        val saldoActual = it.remainingAmount
                        val penalizacionBase = it.penaltyRate
                        val penalizacionExtra = penalizacionAdicional.toDoubleOrNull() ?: 0.0
                        val totalPenalizacion = penalizacionBase + penalizacionExtra
                        val nuevoSaldo = saldoActual * (1 + totalPenalizacion / 100.0)

                        Text("Deuda: ${it.name}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Saldo actual: $${"%.2f".format(saldoActual)}")
                        Text("Penalización base: ${it.penaltyRate}%")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Penalización adicional (%):")
                        OutlinedTextField(
                            value = penalizacionAdicional,
                            onValueChange = { penalizacionAdicional = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("0.0") }
                        )

                        Text("Total penalización: ${"%.1f".format(totalPenalizacion)}%")
                        Text(
                            "Nuevo saldo: $${"%.2f".format(nuevoSaldo)}",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Text("Nueva fecha límite:")
                    OutlinedTextField(
                        value = nuevaFechaRenovar,
                        onValueChange = { nuevaFechaRenovar = it },
                        label = { Text("yyyy-MM-dd") },
                        placeholder = { Text("2024-12-31") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nuevaFechaRenovar.isNotBlank()) {
                            val penalizacionExtra = penalizacionAdicional.toDoubleOrNull() ?: 0.0
                            viewModel.onEvent(
                                DebtEvent.RenovarDeuda(
                                    state.mostrarDialogoRenovar!!,
                                    nuevaFechaRenovar,
                                    penalizacionExtra
                                )
                            )
                            nuevaFechaRenovar = ""
                            penalizacionAdicional = "0.0"
                        }
                    }
                ) {
                    Text("Renovar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.onEvent(DebtEvent.CerrarDialogoRenovar)
                    nuevaFechaRenovar = ""
                    penalizacionAdicional = "0.0"
                }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Deudas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(DebtEvent.MostrarDialogoAgregar) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Deuda")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.cargando) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.deudas.isEmpty()) {
                EmptyDebtsSection { viewModel.onEvent(DebtEvent.MostrarDialogoAgregar) }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.deudas, key = { it.debtId }) { deuda ->
                        DeudaCard(
                            deuda = deuda,
                            onAbonar = { id -> viewModel.onEvent(DebtEvent.MostrarDialogoPago(id)) },
                            onEliminar = { id -> viewModel.onEvent(DebtEvent.EliminarDeuda(id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyDebtsSection(onAdd: () -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("No hay deudas", style = MaterialTheme.typography.bodyLarge)
            Text(
                "Presiona + para agregar una nueva deuda",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun DeudaCard(
    deuda: Debt,
    onAbonar: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirm by remember { mutableStateOf(false) }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("¿Eliminar deuda?") },
            text = {
                Text(
                    if (deuda.remainingAmount > 0)
                        "Aún queda saldo pendiente. ¿Seguro que deseas eliminarla?"
                    else "La deuda ya está saldada. ¿Deseas eliminarla?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEliminar(deuda.debtId)
                        showConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = deuda.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Acreedor: ${deuda.creditor}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = { showConfirm = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Restante hoy",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${"%.2f".format(deuda.remainingAmount)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (deuda.remainingAmount > 0) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Fecha límite",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = deuda.dueDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { 1f - (deuda.remainingAmount / deuda.principalAmount).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Text(
                text = "${((1f - (deuda.remainingAmount / deuda.principalAmount).toFloat()) * 100).toInt()}% completado",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { onAbonar(deuda.debtId) },
                modifier = Modifier.fillMaxWidth(),
                enabled = deuda.remainingAmount > 0
            ) {
                Icon(Icons.Default.Payment, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Abonar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeudaListScreenPreview() {
    FinanzenTheme {
        DebtListScreen(
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DeudaCardPreview() {
    FinanzenTheme {
        DeudaCard(
            deuda = Debt(
                debtId = 1,
                name = "Préstamo bancario",
                creditor = "Banco XYZ",
                principalAmount = 10_000.0,
                remainingAmount = 7_500.0,
                interestRate = 12.5,
                penaltyRate = 3.0,
                dueDate = "2025-12-31",
                compoundingPeriod = CompoundingPeriod.MONTHLY,
                interestType = InterestType.SIMPLE,
                status = DebtStatus.ACTIVE,
                creationDate = "2024-01-01",
                usuarioId = 1
            ),
            onAbonar = {},
            onEliminar = {}
        )
    }
}