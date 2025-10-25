package io.dodn.commerce.storage.db.core

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface SettlementTargetRepository : JpaRepository<SettlementTargetEntity, Long> {
    // 가맹점별 정산 일자에 정산해야 하는 금액을 연산하게 함.
    @Query(
        """
        SELECT new io.dodn.commerce.storage.db.core.SettlementTargetSummary(
            settlement.merchantId,
            settlement.settlementDate,
            SUM(settlement.targetAmount),
            COUNT(settlement.id),
            COUNT(DISTINCT settlement.orderId)
        )
        FROM SettlementTargetEntity settlement
            WHERE settlement.settlementDate = :settlementDate
        GROUP BY settlement.merchantId, settlement.settlementDate
        """,
    )
    fun findSummary(settlementDate: LocalDate): List<SettlementTargetSummary>
}
