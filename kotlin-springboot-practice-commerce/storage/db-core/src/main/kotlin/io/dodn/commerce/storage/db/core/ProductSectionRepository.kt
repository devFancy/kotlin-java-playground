package io.dodn.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface ProductSectionRepository : JpaRepository<ProductSectionEntity, Long> {
    fun findByProductId(productId: Long): List<ProductSectionEntity>
}
