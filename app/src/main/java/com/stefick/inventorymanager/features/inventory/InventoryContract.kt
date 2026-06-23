package com.stefick.inventorymanager.features.inventory

import androidx.paging.PagingData
import com.stefick.core.model.InventoryItem
import kotlinx.coroutines.flow.Flow

sealed class InventoryUiState {
    object Loading : InventoryUiState()
    data class Error(val message: String) : InventoryUiState()
    data class Loaded(
        val items: Flow<PagingData<InventoryItem>>
    ) : InventoryUiState()
}


sealed class InventoryEvent {
    data class AddItem(val item: InventoryItem) : InventoryEvent()
    data class Refresh(val isRefreshing: Boolean) : InventoryEvent()
    data class BarCodeScanned(val barcode: String) : InventoryEvent()
    data class AiSuggestionDetected(val name: String, val confidence: Float) : InventoryEvent()
}