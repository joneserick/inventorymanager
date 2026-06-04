package com.stefick.inventorymanager.features.inventory

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stefick.core.model.InventoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(viewModel: InventoryViewModel = hiltViewModel()) {
    // Coleta o estado de forma segura com o ciclo de vida do Compose
    val items by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Inventário") }) }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(items) { item ->
                InventoryItemRow(item)
            }
        }
    }
}

@Composable
fun InventoryItemRow(item: InventoryItem) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Qtd: ${item.quantity}", style = MaterialTheme.typography.labelMedium)
    }
}