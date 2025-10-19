package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.SettlementState
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(
    name = "settlement",
    indexes = [Index(name = "udx_settlement_merchant", columnList = "settlementDate, merchantId", unique = true)],
)
class SettlementEntity(
    val merchantId: Long,
    val settlementDate: LocalDate,
    val originalAmount: BigDecimal,
    val feeAmount: BigDecimal,
    val feeRate: BigDecimal,
    val settlementAmount: BigDecimal,
    state: SettlementState,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var state: SettlementState = state
        protected set

    fun sent() {
        state = SettlementState.SENT
    }
}
