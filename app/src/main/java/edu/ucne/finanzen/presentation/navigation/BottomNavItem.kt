package edu.ucne.finanzen.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(
    val route: Any,
    val icon: ImageVector,
    val label: String
) {
    Dashboard(Screen.Dashboard, Icons.Default.Home, "Inicio"),
    Transactions(Screen.Transactions, Icons.Default.List, "Transac"),
    Budgets(Screen.Budgets, Icons.Default.AccountBalance, "Presup."),
    Goals(Screen.Goals, Icons.Default.Star, "Metas"),
    Debts(Screen.Debts, Icons.Default.CreditCard, "Deudas"),
    Analysis(Screen.Analysis, Icons.Default.Analytics, "Análisis")
}