package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "product")
class ProductEntity(
    val name: String,
    val thumbnailUrl: String, // 썸네일
    val description: String,
    val shortDescription: String,
    val costPrice: BigDecimal, // 원가
    val salesPrice: BigDecimal, // 판매가
    val discountedPrice: BigDecimal, // 할인가
) : BaseEntity()
