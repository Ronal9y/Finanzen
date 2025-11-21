package edu.ucne.finanzen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.finanzen.Worker.FinanceCheckWorker
import edu.ucne.finanzen.common.NotificationHelper
import edu.ucne.finanzen.presentation.navigation.BottomBar
import edu.ucne.finanzen.presentation.navigation.FinanceNavHost
import edu.ucne.finanzen.presentation.navigation.Screen
import java.util.concurrent.TimeUnit

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
// Crear canal de notificaciones
        NotificationHelper.createNotificationChannel(this)

        // Programar Worker
        val workRequest = PeriodicWorkRequestBuilder<FinanceCheckWorker>(24, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "finance_check",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        // Solicitar permiso en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val bottomBarVisible = currentRoute != Screen.Welcome::class.qualifiedName &&
                    currentRoute != Screen.Login::class.qualifiedName
            Scaffold(
                bottomBar = { if (bottomBarVisible) BottomBar(navController) }
            ) { innerPadding ->
                FinanceNavHost(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}