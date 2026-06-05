package com.stefick.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inventory_items")
data class ItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val sku: String,
    val quantity: Int,
    val price: Double,
    val description: String,
    val barcode: String
)