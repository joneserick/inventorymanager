package com.stefick.inventorymanager.features.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.stefick.inventorymanager.features.agent.AiAgentScreen
import com.stefick.inventorymanager.features.auth.presentation.login.LoginScreen
import com.stefick.inventorymanager.features.inventory.InventoryScreen
import com.stefick.inventorymanager.features.main.MainShellScreen
import com.stefick.inventorymanager.features.navigation.Routes.AiAgent
import com.stefick.inventorymanager.features.navigation.Routes.Home
import com.stefick.inventorymanager.features.navigation.Routes.Inventory
import com.stefick.inventorymanager.features.navigation.Routes.Login
import com.stefick.inventorymanager.features.navigation.Routes.Scanner
import com.stefick.inventorymanager.features.scanner.BarcodeScanner

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Routes = Login
) {
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Login> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(MainShellRoute) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

        composable<MainShellRoute> {
            MainShellScreen(rootNavController = navController)
        }

        composable<Inventory> {
            InventoryScreen(
                onScanClick = {
                    navController.navigate("scanner")
                },
                onNavigateToAiAgent = {
                    navController.navigate("agent")
                }
            )
        }
        composable<Scanner> {
            BarcodeScanner(
                onBarcodeDetected = { barcode ->
                    Toast.makeText(context, "Barcode detected: $barcode", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            )
        }
        composable<AiAgent> {
            AiAgentScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}