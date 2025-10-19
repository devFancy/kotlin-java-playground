package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.Order
import io.dodn.commerce.core.enums.OrderState
import java.math.BigDecimal

data class OrderResponse(
    val key: String,
    val name: String,
    val totalPrice: BigDecimal,
    val state: OrderState,
    val items: List<OrderItemResponse>,
) {
    companion object {
        fun of(order: Order): OrderResponse {
            return OrderResponse(
                key = order.key,
                name = order.name,
                totalPrice = order.totalPrice,
                state = order.state,
                items = order.items.map {
                    OrderItemResponse(
                        productId = it.productId,
                        productName = it.productName,
                        thumbnailUrl = it.thumbnailUrl,
                        shortDescription = it.shortDescription,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                        totalPrice = it.totalPrice,
                    )
                },
            )
        }
    }
}
