package io.dodn.commerce.core.domain

import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType

data class Cart(
    val userId: Long,
    val items: List<CartItem>,
) {
    fun toNewOrder(targetItemIds: Set<Long>): NewOrder {
        if (items.isEmpty()) throw CoreException(ErrorType.INVALID_REQUEST)
        return NewOrder(
            userId = userId,
            items = items.filter { targetItemIds.contains(it.id) }
                .map {
                    NewOrderItem(
                        productId = it.product.id,
                        quantity = it.quantity,
                    )
                },
        )
    }
}
