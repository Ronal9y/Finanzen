package edu.ucne.finanzen.presentation.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.ucne.finanzen.domain.model.CategoryType
import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.model.TransactionType
import edu.ucne.finanzen.ui.theme.FinanzenTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionDialog(
    currentUserId: Int,
    onDismiss: () -> Unit,
    onAdd: (Transaction) -> Unit
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
            Text(
                text = "Nueva Transacción",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            var type by remember { mutableStateOf(TransactionType.EXPENSE) }
            var amount by remember { mutableStateOf("") }
            var category by remember { mutableStateOf(CategoryType.ALIMENTACION) }
            var description by remember { mutableStateOf("") }

            Text("Tipo", style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilterChip(
                    selected = type == TransactionType.INCOME,
                    onClick = {
                        type = TransactionType.INCOME
                        category = CategoryType.SALARIO
                    },
                    label = { Text("Ingreso") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF388E3C),
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = type == TransactionType.EXPENSE,
                    onClick = {
                        type = TransactionType.EXPENSE
                        category = CategoryType.ALIMENTACION
                    },
                    label = { Text("Gasto") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD32F2F),
                        selectedLabelColor = Color.White
                    )
                )
            }

            Text("Monto", style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                placeholder = { Text("0.00") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Categoría", style = MaterialTheme.typography.bodyMedium)
            val categories = remember(type) {
                if (type == TransactionType.INCOME) listOf(
                    CategoryType.SALARIO,
                    CategoryType.BONIFICACIONES,
                    CategoryType.FREELANCE,
                    CategoryType.INVERSIONES,
                    CategoryType.ALQUILERES,
                    CategoryType.REGALOS,
                    CategoryType.COMISIONES,
                    CategoryType.PROYECTOS_ESPECIALES,
                    CategoryType.REEMBOLSOS,
                    CategoryType.SUBSIDIOS
                ) else listOf(
                    CategoryType.ALIMENTACION,
                    CategoryType.VIVIENDA,
                    CategoryType.TRANSPORTE,
                    CategoryType.SALUD,
                    CategoryType.ENTRETENIMIENTO,
                    CategoryType.ROPA_CALZADO,
                    CategoryType.EDUCACION,
                    CategoryType.AHORRO_INVERSION,
                    CategoryType.COMPRAS,
                    CategoryType.CUIDADO_PERSONAL,
                    CategoryType.OTROS_GASTOS
                )
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat.name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (type == TransactionType.INCOME) Color(
                                0xFF388E3C
                            ) else Color(0xFFD32F2F),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Text("Descripción (opcional)", style = MaterialTheme.typography.bodyMedium)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Ej: Almuerzo en restaurante") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(Modifier.width(12.dp))
                Button(
                    modifier = Modifier.height(48.dp),
                    onClick = {
                        val amountDouble = amount.toDoubleOrNull() ?: 0.0
                        if (amountDouble > 0) {
                            onAdd(
                                Transaction(
                                    transactionId = 0,
                                    type = type,
                                    amount = amountDouble,
                                    category = category,
                                    description = description,
                                    usuarioId = currentUserId,
                                    date = SimpleDateFormat(
                                        "yyyy-MM-dd'T'HH:mm:ss",
                                        Locale.getDefault()
                                    ).format(Date())
                                )
                            )
                        }
                    }
                ) {
                    Text("Agregar", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddTransactionDialogPreview() {
    FinanzenTheme {
        AddTransactionDialog(
            currentUserId = 1,
            onDismiss = {},
            onAdd = {}
        )
    }
}