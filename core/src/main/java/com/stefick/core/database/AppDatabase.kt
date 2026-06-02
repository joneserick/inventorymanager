package com.stefick.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stefick.core.database.dao.InventoryDao
import com.stefick.core.database.model.ItemEntity

@Database(entities = [ItemEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun inventoryDao(): InventoryDao
}