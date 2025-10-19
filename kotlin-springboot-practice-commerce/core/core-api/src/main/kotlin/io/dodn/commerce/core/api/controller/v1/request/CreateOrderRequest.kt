package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.NewOrder
import io.dodn.commerce.core.domain.NewOrderItem
import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType

data class CreateOrderRequest(
    val productId: Long,
    val quantity: Long,
) {
    fun toNewOrder(user: User): NewOrder {
        if (quantity <= 0) throw CoreException(ErrorType.INVALID_REQUEST)
        return NewOrder(
            userId = user.id,
            items = listOf(NewOrderItem(productId, quantity)),
        )
    }
}
