package com.stefick.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stefick.core.data.local.CryptoManager
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.security.Security
import javax.crypto.AEADBadTagException

@RunWith(AndroidJUnit4::class)
class CryptoManagerTest {

    private lateinit var cryptoManager: CryptoManager

    @Before
    fun setUp() {
        Security.addProvider(BouncyCastleProvider())
        cryptoManager = CryptoManager()
    }

    @Test
    fun encryptAndDecryptShouldReturnTheExactOriginalString() {
        // Arrange
        val originalText = "super_secret_token_123"

        // Act
        val encryptedBase64 = cryptoManager.encrypt(originalText)
        val decryptedText = cryptoManager.decrypt(encryptedBase64)

        // Assert
        Assert.assertNotEquals(originalText, encryptedBase64) // Garante que foi embaralhado
        Assert.assertEquals(originalText, decryptedText)      // Garante a reversibilidade perfeita
    }

    @Test
    fun encryptShouldProduceDifferentCipherTextsForTheSameInputDueToDynamicIV() {
        // Arrange
        val text = "same_password"

        // Act
        val encryption1 = cryptoManager.encrypt(text)
        val encryption2 = cryptoManager.encrypt(text)

        // Assert
        Assert.assertNotEquals(encryption1, encryption2)
    }

    @Test
    fun decryptingCorruptedOrForgedDataShouldThrowAEADBadTagException() {
        // Arrange
        val validEncryptedText = cryptoManager.encrypt("valid_data")

        // Vamos ser sádicos e mudar apenas 1 caractere da string Base64 (ataque de corrupção)
        val forgedEncryptedText = validEncryptedText.dropLast(2) + "=="

        // Act & Assert
        Assert.assertThrows(AEADBadTagException::class.java) {
            cryptoManager.decrypt(forgedEncryptedText)
        }
    }
}