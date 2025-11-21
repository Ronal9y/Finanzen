package edu.ucne.finanzen.presentation.dashboard

sealed interface DashboardEvent {
    object Refresh : edu.ucne.finanzen.presentation.dashboard.DashboardEvent
}