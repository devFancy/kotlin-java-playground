package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.CartItem
import java.math.BigDecimal

data class CartResponse(
    val items: List<CartItemResponse>,
)

data class CartItemResponse(
    val id: Long,
    val productId: Long,
    val productName: String,
    val thumbnailUrl: String,
    val description: String,
    val shortDescription: String,
    val costPrice: BigDecimal,
    val salesPrice: BigDecimal,
    val discountedPrice: BigDecimal,
    val quantity: Long,
) {
    companion object {
        fun of(cartItem: CartItem): CartItemResponse = CartItemResponse(
            id = cartItem.id,
            productId = cartItem.product.id,
            productName = cartItem.product.name,
            thumbnailUrl = cartItem.product.thumbnailUrl,
            description = cartItem.product.description,
            shortDescription = cartItem.product.shortDescription,
            costPrice = cartItem.product.price.costPrice,
            salesPrice = cartItem.product.price.salesPrice,
            discountedPrice = cartItem.product.price.discountedPrice,
            quantity = cartItem.quantity,
        )
    }
}
