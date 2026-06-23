package com.stefick.core.data.repository.di

import com.stefick.core.data.local.SessionStorage
import com.stefick.core.data.repository.AuthRepository
import com.stefick.core.data.repository.AuthRepositoryImpl
import com.stefick.core.database.AppDatabase
import com.stefick.core.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideAuthRepository(
        database: AppDatabase,
        sessionStorage: SessionStorage
    ): AuthRepository {
        return AuthRepositoryImpl(
            userDao = database.userDao(),
            sessionStorage = sessionStorage
        )
    }

}