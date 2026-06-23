package com.stefick.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stefick.core.database.dao.InventoryDao
import com.stefick.core.database.dao.UserDao
import com.stefick.core.database.entity.ItemEntity
import com.stefick.core.database.entity.UserEntity

@Database(entities = [ItemEntity::class, UserEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun inventoryDao(): InventoryDao
    abstract fun userDao(): UserDao
}