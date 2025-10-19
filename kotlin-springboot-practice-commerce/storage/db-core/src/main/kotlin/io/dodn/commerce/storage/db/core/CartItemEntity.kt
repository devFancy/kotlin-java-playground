package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "cart_item")
class CartItemEntity(
    val userId: Long,
    val productId: Long,
    quantity: Long,
) : BaseEntity() {
    var quantity: Long = quantity
        protected set

    fun applyQuantity(value: Long) {
        this.quantity = if (value < 1) 1 else value
    }
}
