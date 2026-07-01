package com.stefick.inventorymanager.features.home.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stefick.core.model.UserRole

@Composable
fun HomeRouterScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val role by viewModel.userRole.collectAsStateWithLifecycle()

    when (role) {
        UserRole.ADMIN, UserRole.SUPERVISOR -> {
            ManagerDashboard()
        }
        UserRole.STOCKER, UserRole.VENDOR -> {
            OperatorDashboard()
        }
        null -> {
            // Estado de carregamento rápido enquanto o DataStore devolve o ficheiro
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

// Os esqueletos dos painéis que vamos desenhar a seguir
@Composable
fun ManagerDashboard() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Painel do Diretor Financeiro (Gráficos e Dinheiro)")
    }
}

@Composable
fun OperatorDashboard() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Painel do Operador (Trincheiras: Entradas e Saídas)")
    }
}