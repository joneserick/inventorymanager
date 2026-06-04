package com.stefick.core.database.di

import android.app.Application
import androidx.room.Room
import com.stefick.core.database.AppDatabase
import com.stefick.core.database.dao.InventoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "inventory_db"
        ).build()
    }

    @Provides
    fun provideInventoryDao(database: AppDatabase): InventoryDao {
        return database.inventoryDao()
    }

}