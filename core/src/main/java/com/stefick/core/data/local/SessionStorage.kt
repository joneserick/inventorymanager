package com.stefick.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_user_session")

data class SessionData(val token: String, val role: String, val email: String)

@Singleton
class SessionStorage @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cryptoManager: CryptoManager
) {
    companion object {
        private val KEY_TOKEN = stringPreferencesKey("enc_jwt_token")
        private val KEY_ROLE = stringPreferencesKey("enc_user_role")
        private val KEY_EMAIL = stringPreferencesKey("enc_user_email")
    }

    val sessionFlow: Flow<SessionData?> = context.dataStore.data.map { preferences ->
        val encryptedToken = preferences[KEY_TOKEN]
        val encryptedRole = preferences[KEY_ROLE]
        val encryptedEmail = preferences[KEY_EMAIL]

        if (encryptedToken != null && encryptedRole != null && encryptedEmail != null) {
            try {
                SessionData(
                    token = cryptoManager.decrypt(encryptedToken),
                    role = cryptoManager.decrypt(encryptedRole),
                    email = cryptoManager.decrypt(encryptedEmail)
                )
            } catch (e: Exception) {
                clearSession()
                null
            }
        } else {
            null
        }
    }

    suspend fun saveSession(token: String, role: String, userEmail: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TOKEN] = cryptoManager.encrypt(token)
            preferences[KEY_ROLE] = cryptoManager.encrypt(role)
            preferences[KEY_EMAIL] = cryptoManager.encrypt(userEmail)
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}