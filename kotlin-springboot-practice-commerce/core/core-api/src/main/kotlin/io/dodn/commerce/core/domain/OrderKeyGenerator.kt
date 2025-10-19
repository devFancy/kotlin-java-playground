package io.dodn.commerce.core.domain

import org.springframework.stereotype.Component
import java.nio.ByteBuffer
import java.util.Base64
import java.util.UUID

@Component
class OrderKeyGenerator {
    fun generate(): String {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(
            ByteBuffer.allocate(16).apply {
                UUID.randomUUID().also {
                    putLong(it.mostSignificantBits)
                    putLong(it.leastSignificantBits)
                }
            }.array(),
        )
    }
}
