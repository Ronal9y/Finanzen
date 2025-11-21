package edu.ucne.finanzen.presentation.budgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.ucne.finanzen.domain.model.CategoryType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetDialog(
    onDismiss: () -> Unit,
    onConfirm: (CategoryType, Double, Int) -> Unit
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
                text = "Nuevo Presupuesto",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            var selectedCategory by remember { mutableStateOf(CategoryType.ALIMENTACION) }
            var limit by remember { mutableStateOf("") }
            var threshold by remember { mutableStateOf("80") }

            val categories = remember {
                listOf(
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

            // Selector de categoría
            Text(
                text = "Categoría",
                style = MaterialTheme.typography.titleMedium
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category.name) }
                    )
                }
            }

            OutlinedTextField(
                value = limit,
                onValueChange = { limit = it },
                label = { Text("Límite mensual ($)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = threshold,
                onValueChange = { threshold = it },
                label = { Text("Alerta al (%)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        val limitDouble = limit.toDoubleOrNull() ?: 0.0
                        val thresholdInt = threshold.toIntOrNull() ?: 80
                        if (limitDouble > 0) {
                            onConfirm(selectedCategory, limitDouble, thresholdInt)
                        }
                    }
                ) {
                    Text("Crear")
                }
            }
        }
    }
}