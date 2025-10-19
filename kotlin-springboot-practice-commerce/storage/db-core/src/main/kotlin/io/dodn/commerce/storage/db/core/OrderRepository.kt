package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OrderState
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<OrderEntity, Long> {
    fun findByOrderKeyAndStateAndStatus(orderKey: String, state: OrderState, status: EntityStatus): OrderEntity?
    fun findByUserIdAndStateAndStatusOrderByIdDesc(userId: Long, state: OrderState, status: EntityStatus): List<OrderEntity>
}
