package io.dodn.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository

interface PointBalanceRepository : JpaRepository<PointBalanceEntity, Long> {
    fun findByUserId(userId: Long): PointBalanceEntity?
}
