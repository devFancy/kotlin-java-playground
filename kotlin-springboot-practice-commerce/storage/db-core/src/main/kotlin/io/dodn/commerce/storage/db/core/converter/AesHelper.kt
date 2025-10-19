package io.dodn.commerce.storage.db.core.converter

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
internal class AesHelper(
    private val property: SecurityProperty,
) {
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val ALGORITHM = "AES"
        private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    }

    fun encrypt(target: String): String {
        try {
            val key = SecretKeySpec(property.key.toByteArray(), ALGORITHM)
            val iv = IvParameterSpec(property.iv.toByteArray())

            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, key, iv)

            val encrypted = cipher.doFinal(target.toByteArray())
            return Base64.getEncoder().encodeToString(encrypted)
        } catch (e: Exception) {
            log.error("AesHelper.encrypt Exception: {}", e.message, e)
            return target
        }
    }

    fun decrypt(target: String): String {
        try {
            val key = SecretKeySpec(property.key.toByteArray(), ALGORITHM)
            val iv = IvParameterSpec(property.iv.toByteArray())

            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, key, iv)

            val decrypted = cipher.doFinal(Base64.getDecoder().decode(target))
            return String(decrypted)
        } catch (e: Exception) {
            log.error("AesHelper.decrypt Exception: {}", e.message, e)
            return target
        }
    }
}
