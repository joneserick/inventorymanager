package com.stefick.inventorymanager.features.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.stefick.core.data.InventoryRepository
import com.stefick.core.model.InventoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val repository: InventoryRepository
) : ViewModel() {
    val pagedItems = repository.getInventoryStream().cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow<InventoryUiState>(InventoryUiState.Loaded(pagedItems))
    val uiState: StateFlow<InventoryUiState> = _uiState.asStateFlow()

    fun onEvent(event: InventoryEvent) {
        when (event) {
            is InventoryEvent.AddItem -> addItem(event.item)
            is InventoryEvent.Refresh -> handleRefresh()
            is InventoryEvent.BarCodeScanned -> Unit
            is InventoryEvent.AiSuggestionDetected -> Unit
        }
    }

    private fun addItem(item: InventoryItem) {
        viewModelScope.launch { repository.addItem(item) }
    }

    private fun handleRefresh() {
        _uiState.update { InventoryUiState.Loading }
    }
}