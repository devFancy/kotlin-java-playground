package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.OrderSummary
import io.dodn.commerce.core.enums.OrderState
import java.math.BigDecimal

data class OrderListResponse(
    val key: String,
    val name: String,
    val totalPrice: BigDecimal,
    val state: OrderState,
) {
    companion object {
        private fun of(order: OrderSummary): OrderListResponse {
            return OrderListResponse(
                key = order.key,
                name = order.name,
                totalPrice = order.totalPrice,
                state = order.state,
            )
        }

        fun of(orders: List<OrderSummary>): List<OrderListResponse> {
            return orders.map { of(it) }
        }
    }
}
