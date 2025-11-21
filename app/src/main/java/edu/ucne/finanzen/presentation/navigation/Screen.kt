package edu.ucne.finanzen.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen() {
    @Serializable
    data object Dashboard : Screen()

    @Serializable
    data object Transactions : Screen()

    @Serializable
    data object Budgets : Screen()

    @Serializable
    data object AddBudget : Screen()

    @Serializable
    data object Goals : Screen()

    @Serializable
    data object AddGoal : Screen()

    @Serializable
    data object Debts : Screen()

    @Serializable
    data object AddDebt : Screen()
    @Serializable
    data object Analysis : Screen()

    @Serializable
    data object Welcome : Screen()

    @Serializable
    data object Login : Screen()

}