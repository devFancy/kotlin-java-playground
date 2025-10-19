package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "merchant_product_mapping",
    indexes = [
        Index(name = "udx_merchant_product", columnList = "merchantId, productId", unique = true),
    ],
)
class MerchantProductMappingEntity(
    val productId: Long,
    val merchantId: Long,
) : BaseEntity()
