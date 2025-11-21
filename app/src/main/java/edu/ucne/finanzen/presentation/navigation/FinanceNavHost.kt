package edu.ucne.finanzen.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.finanzen.presentation.analysis.AnalysisScreen
import edu.ucne.finanzen.presentation.budgets.BudgetsScreen
import edu.ucne.finanzen.presentation.dashboard.DashboardScreen
import edu.ucne.finanzen.presentation.debts.DebtListScreen
import edu.ucne.finanzen.presentation.goals.GoalsScreen
import edu.ucne.finanzen.presentation.login.LoginScreen
import edu.ucne.finanzen.presentation.login.WelcomeScreen
import edu.ucne.finanzen.presentation.transactions.TransactionsScreen

@Composable
fun FinanceNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Welcome,
        modifier = modifier
    ) {

        composable<Screen.Welcome> {
            WelcomeScreen(
                onStartWithoutAccount = {
                    navController.navigate(Screen.Dashboard) {
                        popUpTo(Screen.Welcome) { inclusive = true }
                    }
                },
                onGoToLogin = {
                    navController.navigate(Screen.Login)
                }
            )
        }
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard) {
                        popUpTo(Screen.Welcome) { inclusive = true }
                    }
                }
            )
        }

        composable<Screen.Dashboard> {
            DashboardScreen(
                onAddTransaction = {  },
                onViewTransactions = { navController.navigate(Screen.Transactions) },
                onViewGoals = { navController.navigate(Screen.Goals) },
                onViewDebts = { navController.navigate(Screen.Analysis) }
            )
        }

        composable<Screen.Transactions> {
            TransactionsScreen(
                onBack = { navController.popBackStack() },
                onAddTransaction = {  }
            )
        }

        composable<Screen.Budgets> {
            BudgetsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable<Screen.Goals> {
            GoalsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Debts> {
            DebtListScreen(
                onBack = { navController.popBackStack() }

            )
        }
        composable<Screen.Analysis> {
            AnalysisScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}