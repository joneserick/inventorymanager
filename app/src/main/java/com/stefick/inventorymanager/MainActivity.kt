package com.stefick.inventorymanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stefick.inventorymanager.features.agent.AiAgentScreen
import com.stefick.inventorymanager.features.inventory.InventoryScreen
import com.stefick.inventorymanager.features.scanner.BarcodeScanner
import com.stefick.inventorymanager.ui.theme.InventoryManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryManagerTheme {
                val context = LocalContext.current
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "inventory",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("inventory") {
                            InventoryScreen(
                                onScanClick = {
                                    navController.navigate("scanner")
                                },
                                onNavigateToAiAgent = {
                                    navController.navigate("agent")
                                }
                            )
                        }
                        composable("scanner") {
                            BarcodeScanner(
                                onBarcodeDetected = { barcode ->
                                    Toast.makeText(context, "Barcode detected: $barcode", Toast.LENGTH_LONG).show()
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("agent") {
                            AiAgentScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
