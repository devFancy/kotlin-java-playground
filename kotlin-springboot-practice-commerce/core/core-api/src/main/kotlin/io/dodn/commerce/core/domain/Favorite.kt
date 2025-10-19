package io.dodn.commerce.core.domain

import java.time.LocalDateTime

data class Favorite(
    val id: Long,
    val userId: Long,
    val productId: Long,
    val favoritedAt: LocalDateTime,
)
