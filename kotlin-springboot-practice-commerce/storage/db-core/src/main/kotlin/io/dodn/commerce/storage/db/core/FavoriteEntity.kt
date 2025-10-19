package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "favorite")
class FavoriteEntity(
    val userId: Long,
    val productId: Long,
    favoritedAt: LocalDateTime,
) : BaseEntity() {
    var favoritedAt: LocalDateTime = favoritedAt
        protected set

    fun favorite() {
        active()
        favoritedAt = LocalDateTime.now()
    }
}
