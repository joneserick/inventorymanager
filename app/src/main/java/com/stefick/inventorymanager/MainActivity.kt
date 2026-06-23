package com.stefick.inventorymanager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stefick.inventorymanager.features.agent.AiAgentScreen
import com.stefick.inventorymanager.features.inventory.InventoryScreen
import com.stefick.inventorymanager.features.navigation.AppNavHost
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
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()) {
                        AppNavHost(navController)
                    }
                }
            }
        }
    }
}
