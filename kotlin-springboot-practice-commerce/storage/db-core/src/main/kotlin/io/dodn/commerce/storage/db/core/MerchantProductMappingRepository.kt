package io.dodn.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface MerchantProductMappingRepository : JpaRepository<MerchantProductMappingEntity, Long> {
    fun findByProductIdIn(productIds: Set<Long>): List<MerchantProductMappingEntity>
}
