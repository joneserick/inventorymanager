package com.stefick.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
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
            InventoryItem(
                id = entity.id,
                name = entity.name,
                sku = entity.sku,
                quantity = entity.quantity,
                price = entity.price,
                description = entity.description,
                barcode = entity.barcode
            )
        }
    }

    fun getInventoryStream(): Flow<PagingData<InventoryItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { dao.getPagedItems() }
        ).flow.map { pagingData ->
            pagingData.map { entity ->
                InventoryItem(
                    id = entity.id,
                    name = entity.name,
                    sku = entity.sku,
                    quantity = entity.quantity,
                    price = entity.price,
                    description = entity.description,
                    barcode = entity.barcode
                )
            }
        }
    }

    suspend fun addItem(item: InventoryItem) {
        dao.insertItem(
            ItemEntity(
                id = item.id,
                name = item.name,
                sku = item.sku,
                quantity = item.quantity,
                price = item.price,
                description = item.description,
                barcode = item.barcode
            )
        )
    }
}