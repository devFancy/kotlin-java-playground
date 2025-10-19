package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemRepository : JpaRepository<CartItemEntity, Long> {
    fun findByUserIdAndStatus(userId: Long, status: EntityStatus): List<CartItemEntity>
    fun findByUserIdAndIdAndStatus(userId: Long, id: Long, status: EntityStatus): CartItemEntity?
    fun findByUserIdAndProductId(userId: Long, productId: Long): CartItemEntity?
}
