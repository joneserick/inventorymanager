package com.stefick.inventorymanager.features.inventory

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.stefick.core.model.InventoryItem
import kotlin.contracts.contract

@Composable
fun InventoryScreen(viewModel: InventoryViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is InventoryUiState.Loading -> {}
        is InventoryUiState.Error -> {}
        is InventoryUiState.Loaded -> {
            val pagedItems = state.items.collectAsLazyPagingItems()
            InventoryList(items = pagedItems, onEvent = viewModel::onEvent)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryList(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<InventoryItem>,
    onEvent: (InventoryEvent) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Inventário") }) }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(
                count = items.itemCount,
                key = items.itemKey { it.id },
                contentType = items.itemContentType { "item" }

            ) { index ->
                val item = items[index]
                if (item == null) return@items
                InventoryItemRow(item)
            }

            if (items.loadState.append is LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                16.dp
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun InventoryItemRow(item: InventoryItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "Qtd: ${item.quantity}", style = MaterialTheme.typography.labelMedium)
    }
}