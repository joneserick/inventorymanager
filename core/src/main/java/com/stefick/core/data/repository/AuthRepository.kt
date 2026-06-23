package com.stefick.core.data.repository

import com.stefick.core.model.AuthResult
import com.stefick.core.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentSession: Flow<UserSession?>

    suspend fun login(email: String, passwordRaw: String): AuthResult?
    suspend fun logout()
    suspend fun isUserLoggedIn(): Boolean
}