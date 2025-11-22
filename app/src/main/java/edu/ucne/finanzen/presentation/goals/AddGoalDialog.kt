package edu.ucne.finanzen.presentation.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, target: Double, desc: String, deadline: String) -> Unit
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
                text = "Nueva Meta",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            var name by remember { mutableStateOf("") }
            var target by remember { mutableStateOf("") }
            var desc by remember { mutableStateOf("") }
            var deadline by remember { mutableStateOf("") }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la Meta") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = target,
                onValueChange = { target = it },
                label = { Text("Monto Objetivo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = deadline,
                onValueChange = { deadline = it },
                label = { Text("Fecha límite (YYYY-MM-DD)") },
                placeholder = { Text("Ej: 2024-12-31") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = desc,
                onValueChange = { desc = it },
                label = { Text("Descripción (opcional)") },
                singleLine = true,
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
                        val targetDouble = target.toDoubleOrNull() ?: 0.0
                        if (name.isNotBlank() && targetDouble > 0) {
                            // Convertir fecha a formato ISO
                            val isoDeadline = convertToISODate(deadline)
                            println("🔍 Fecha enviada a API: '$isoDeadline'")
                            onConfirm(name, targetDouble, desc, isoDeadline)
                            onDismiss() // Cerrar diálogo después de crear
                        }
                    }
                ) {
                    Text("Crear")
                }
            }
        }
    }
}


fun convertToISODate(dateString: String): String {
    return try {
        if (dateString.isBlank()) {

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, 6)
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            formatter.format(calendar.time)
        } else if (dateString.contains("-") && dateString.length == 10) {

            "${dateString}T00:00:00"
        } else {

            val inputFormats = listOf(
                SimpleDateFormat("dd MMM yyyy", Locale.getDefault()),
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
                SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            )

            var parsedDate: Date? = null
            for (format in inputFormats) {
                try {
                    parsedDate = format.parse(dateString)
                    if (parsedDate != null) break
                } catch (e: Exception) {

                }
            }

            if (parsedDate != null) {
                val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                outputFormat.format(parsedDate)
            } else {

                "2024-12-31T00:00:00"
            }
        }
    } catch (e: Exception) {

        "2024-12-31T00:00:00"
    }
}