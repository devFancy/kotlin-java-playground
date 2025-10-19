package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * Note:
 * 매핑 테이블 -> 누가 더 하위 계념인가 -> 1/2급 개념
 * 카테고리는 상품의 부수 정보라고 생각했음 -> product_category
 */
@Entity
@Table(name = "product_category")
class ProductCategoryEntity(
    val productId: Long,
    val categoryId: Long,
) : BaseEntity()
