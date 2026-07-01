package com.stefick.inventorymanager.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stefick.inventorymanager.features.home.presentation.HomeRouterScreen
import com.stefick.inventorymanager.features.navigation.AiAgentRoute
import com.stefick.inventorymanager.features.navigation.TopLevelDestination
import com.stefick.inventorymanager.features.navigation.bottomNavItems

@Composable
fun MainShellScreen(
    rootNavController: NavHostController // Usado para navegar para ecrãs fora do BottomNav (ex: IA, Logout)
) {
    // O NavController exclusivo para as abas inferiores
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar {
                bottomNavItems.forEach { item ->
                    // Validação Type-Safe: Verifica se a rota atual pertence a esta aba
                    val isSelected = currentDestination?.hierarchy?.any {
                        it.route?.contains(item.route::class.qualifiedName ?: "") == true
                    } == true

                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = isSelected,
                        onClick = {
                            bottomNavController.navigate(item.route) {
                                // Padrão de navegação inteligente para BottomTabs
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { rootNavController.navigate(AiAgentRoute) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Falar com Agente IA")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        // O NavHost interno que troca o miolo do ecrã
        NavHost(
            navController = bottomNavController,
            startDestination = TopLevelDestination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<TopLevelDestination.Home> {
                HomeRouterScreen() // O cérebro que decide qual Home mostrar
            }
            composable<TopLevelDestination.Catalog> {
                Text("Ecrã do Catálogo (Em breve)")
            }
            composable<TopLevelDestination.Scanner> {
                Text("Ecrã do Scanner de Códigos (Em breve)")
            }
            composable<TopLevelDestination.Profile> {
                Text("Perfil e Definições (Em breve)")
            }
        }
    }
}