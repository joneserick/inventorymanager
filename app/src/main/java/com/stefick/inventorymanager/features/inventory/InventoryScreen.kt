package com.stefick.inventorymanager.features.inventory

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LinkedCamera
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.stefick.core.model.InventoryItem

@Composable
fun InventoryScreen(
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.onEvent(
            InventoryEvent.AddItem(
                InventoryItem(
                    name = "Item 1",
                    description = "Descrição do Item 1",
                    quantity = 5,
                    id = "183jjlf",
                    barcode = "1234567890",
                    price = 10.0,
                    sku = "SKU123"
                )
            )
        )
    }

    when (val state = uiState) {
        is InventoryUiState.Loading -> {
            // Show loading if needed
        }

        is InventoryUiState.Error -> {
            // Show error if needed
        }

        is InventoryUiState.Loaded -> {
            val pagedItems = state.items.collectAsLazyPagingItems()
            InventoryList(
                onScanClick = onScanClick,
                modifier = modifier,
                items = pagedItems,
                onEvent = viewModel::onEvent
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryList(
    onScanClick: () -> Unit,
    modifier: Modifier = Modifier,
    items: LazyPagingItems<InventoryItem>,
    onEvent: (InventoryEvent) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onScanClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventário") },
                actions = {
                    IconButton(onClick = {
                        val permissionCheckResult = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        )
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            onScanClick()
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.LinkedCamera,
                            contentDescription = "Scan Barcode"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(contentPadding = padding, modifier = modifier) {
            items(
                count = items.itemCount,
                key = items.itemKey { it.id },
                contentType = items.itemContentType { "item" }
            ) { index ->
                val item = items[index]
                if (item != null) {
                    InventoryItemRow(item)
                }
            }

            if (items.loadState.append is LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
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
