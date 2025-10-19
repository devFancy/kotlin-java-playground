package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.SettlementState
import org.springframework.data.jpa.repository.JpaRepository

interface SettlementRepository : JpaRepository<SettlementEntity, Long> {
    fun findByState(state: SettlementState): List<SettlementEntity>
}
