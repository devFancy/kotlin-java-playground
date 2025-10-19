package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.CouponTargetType
import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository

interface CouponTargetRepository : JpaRepository<CouponTargetEntity, Long> {
    fun findByTargetTypeAndTargetIdInAndStatus(target: CouponTargetType, targetId: Collection<Long>, status: EntityStatus): List<CouponTargetEntity>
}
