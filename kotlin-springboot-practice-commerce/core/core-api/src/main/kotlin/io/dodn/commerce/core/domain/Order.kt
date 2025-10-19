package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.OrderState
import java.math.BigDecimal

data class Order(
    val id: Long,
    val key: String,
    val name: String,
    val userId: Long,
    val totalPrice: BigDecimal,
    val state: OrderState,
    val items: List<OrderItem>,
)
