package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.Order
import io.dodn.commerce.core.domain.OwnedCoupon
import io.dodn.commerce.core.domain.PointBalance
import java.math.BigDecimal

data class OrderCheckoutResponse(
    val key: String,
    val name: String,
    val totalPrice: BigDecimal,
    val items: List<OrderItemResponse>,
    val usableCoupons: List<OwnedCouponResponse>,
    val usablePoint: BigDecimal,
) {
    companion object {
        fun of(order: Order, coupons: List<OwnedCoupon>, point: PointBalance): OrderCheckoutResponse {
            return OrderCheckoutResponse(
                key = order.key,
                name = order.name,
                totalPrice = order.totalPrice,
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
                usableCoupons = OwnedCouponResponse.of(coupons),
                usablePoint = point.balance,
            )
        }
    }
}
