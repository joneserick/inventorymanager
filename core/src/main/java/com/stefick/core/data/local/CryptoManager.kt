package com.stefick.core.data.local

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoManager @Inject constructor() {

    companion object {
        private const val KEY_ALIAS = "session_encryption_key"
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val IV_SIZE_BYTES = 12
        private const val TAG_SIZE_BITS = 128
    }

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
        load(null)
    }

    private fun getOrCreateKey(): SecretKey {
        val existingKey = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (existingKey != null) return existingKey

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(spec)
        return keyGenerator.generateKey()
    }

    fun encrypt(rawText: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())

        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(rawText.toByteArray(Charsets.UTF_8))

        val combinedBytes = ByteArray(iv.size + encryptedBytes.size)
        System.arraycopy(iv, 0, combinedBytes, 0, iv.size)
        System.arraycopy(encryptedBytes, 0, combinedBytes, iv.size, encryptedBytes.size)

        return Base64.encodeToString(combinedBytes, Base64.NO_WRAP)
    }

    fun decrypt(encryptedBase64: String): String {
        val combinedBytes = Base64.decode(encryptedBase64, Base64.NO_WRAP)

        val iv = ByteArray(IV_SIZE_BYTES)
        val encryptedBytes = ByteArray(combinedBytes.size - IV_SIZE_BYTES)

        // Separa os arrays de volta
        System.arraycopy(combinedBytes, 0, iv, 0, iv.size)
        System.arraycopy(combinedBytes, iv.size, encryptedBytes, 0, encryptedBytes.size)

        val cipher = Cipher.getInstance(ALGORITHM)
        val spec = GCMParameterSpec(TAG_SIZE_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), spec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }
}