package io.dodn.commerce.core.domain

data class CartItem(
    val id: Long,
    val product: Product,
    val quantity: Long,
)
