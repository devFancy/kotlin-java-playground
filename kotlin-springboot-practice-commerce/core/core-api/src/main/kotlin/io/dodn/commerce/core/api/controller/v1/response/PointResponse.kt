package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.PointBalance
import io.dodn.commerce.core.domain.PointHistory
import io.dodn.commerce.core.enums.PointType
import java.math.BigDecimal
import java.time.LocalDateTime

data class PointResponse(
    val userId: Long,
    val balance: BigDecimal,
    val histories: List<PointHistoryResponse>,
) {
    companion object {
        fun of(balance: PointBalance, histories: List<PointHistory>): PointResponse {
            return PointResponse(
                userId = balance.userId,
                balance = balance.balance,
                histories = histories.map {
                    PointHistoryResponse(
                        type = it.type,
                        amount = it.amount,
                        appliedAt = it.appliedAt,
                    )
                },
            )
        }
    }
}

data class PointHistoryResponse(
    val type: PointType,
    val amount: BigDecimal,
    val appliedAt: LocalDateTime,
)
