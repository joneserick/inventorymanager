package com.stefick.core.data

import com.stefick.core.database.dao.InventoryDao
import com.stefick.core.database.model.ItemEntity
import com.stefick.core.model.InventoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InventoryRepository @Inject constructor(
    private val dao: InventoryDao
) {
    val inventoryItems: Flow<List<InventoryItem>> = dao.getAllItems().map { entities ->
        entities.map { entity ->
            InventoryItem(entity.id, entity.name, entity.sku, entity.quantity, entity.price)
        }
    }

    suspend fun addItem(item: InventoryItem) {
        dao.insertItem(ItemEntity(item.id, item.name, item.sku, item.quantity, item.price))
    }
}