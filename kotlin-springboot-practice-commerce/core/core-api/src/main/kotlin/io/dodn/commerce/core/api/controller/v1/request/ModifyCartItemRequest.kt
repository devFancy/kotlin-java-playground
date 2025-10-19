package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.ModifyCartItem

data class ModifyCartItemRequest(
    val quantity: Long,
) {
    fun toModifyCartItem(cartItemId: Long): ModifyCartItem {
        return ModifyCartItem(cartItemId, quantity)
    }
}
