package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.ProductSectionType
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "product_section")
class ProductSectionEntity(
    val productId: Long,
    val type: ProductSectionType,
    val content: String,
) : BaseEntity()
