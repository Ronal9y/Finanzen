package edu.ucne.finanzen.presentation.debts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.ucne.finanzen.domain.model.CompoundingPeriod
import edu.ucne.finanzen.domain.model.Debt
import edu.ucne.finanzen.domain.model.DebtStatus
import edu.ucne.finanzen.domain.model.InterestType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebtAddBottomSheet(
    onDismiss: () -> Unit,
    onConfirm: (Debt) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Nueva Deuda", style = MaterialTheme.typography.headlineSmall)

            var nombre by remember { mutableStateOf("") }
            var acreedor by remember { mutableStateOf("") }
            var monto by remember { mutableStateOf("") }
            var fecha by remember { mutableStateOf("") }

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del préstamo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = acreedor,
                onValueChange = { acreedor = it },
                label = { Text("Acreedor") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = monto,
                onValueChange = { monto = it },
                label = { Text("Monto total") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha límite (yyyy-MM-dd)") },
                placeholder = { Text("2024-12-31") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) { Text("Cancelar") }
                Spacer(Modifier.width(12.dp))
                Button(
                    onClick = {
                        val principal = monto.toDoubleOrNull() ?: 0.0
                        if (principal > 0 && nombre.isNotBlank() && fecha.isNotBlank()) {
                            val fechaISO = if (fecha.length == 10 && fecha.contains("-")) {
                                "${fecha}T00:00:00"
                            } else {
                                "2024-12-31T00:00:00"
                            }

                            onConfirm(
                                Debt(
                                    name = nombre,
                                    principalAmount = principal,
                                    interestRate = null,
                                    interestType = InterestType.SIMPLE,
                                    compoundingPeriod = CompoundingPeriod.MONTHLY,
                                    dueDate = fechaISO,
                                    remainingAmount = principal,
                                    creditor = acreedor,
                                    status = DebtStatus.ACTIVE,
                                    penaltyRate = 0.0,
                                    usuarioId = 0
                                )
                            )
                        }
                    }
                ) {
                    Text("Crear")
                }
            }
        }
    }
}