package com.stefick.core.data.repository

import com.stefick.core.data.local.SessionStorage
import com.stefick.core.database.dao.UserDao
import com.stefick.core.model.AuthResult
import com.stefick.core.model.UserRole
import com.stefick.core.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionStorage: SessionStorage,
) : AuthRepository {

    override val currentSession: Flow<UserSession?> = sessionStorage.sessionFlow.map { data ->
        if (data != null) {
            UserSession(
                id = "user-id",
                name = data.email.substringBefore("@"),
                email = data.email,
                role = UserRole.valueOf(data.role),
                token = data.token
            )
        } else {
            null
        }
    }

    override suspend fun login(email: String, passwordRaw: String): AuthResult? {
        val hashInput = hashPassword(passwordRaw)

        // Tenta o Login Offline primeiro (Room)
        val localUser = userDao.getUserByEmail(email)

        if (localUser != null) {
            if (localUser.passwordHash == hashInput) {
                val mockToken = "LOCAL_JWT_${UUID.randomUUID()}"

                // Apenas mandamos gravar. O currentSession vai emitir o novo estado sozinho!
                sessionStorage.saveSession(mockToken, localUser.role.name, localUser.email)

                return AuthResult.Success(
                    UserSession(localUser.id, localUser.name, localUser.email, localUser.role, mockToken)
                )
            } else {
                return AuthResult.InvalidCredentials
            }
        }

        return null
    }

    override suspend fun logout() {
        // Ao limpar, o DataStore notifica o Flow, que emite null, e a UI chuta o utilizador para a tela de Login
        sessionStorage.clearSession()
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        val session = sessionStorage.sessionFlow.firstOrNull()
        return session != null
    }
}