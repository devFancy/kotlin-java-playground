package io.dodn.commerce.core.domain

import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.PointBalanceRepository
import io.dodn.commerce.storage.db.core.PointHistoryRepository
import org.springframework.stereotype.Component

@Component
class PointService(
    private val pointBalanceRepository: PointBalanceRepository,
    private val pointHistoryRepository: PointHistoryRepository,
) {
    fun balance(user: User): PointBalance {
        val found = pointBalanceRepository.findByUserId(user.id) ?: throw CoreException(ErrorType.NOT_FOUND_DATA)
        return PointBalance(
            userId = found.userId,
            balance = found.balance,
        )
    }

    fun histories(user: User): List<PointHistory> {
        return pointHistoryRepository.findByUserId(user.id)
            .map {
                PointHistory(
                    id = it.id,
                    userId = it.userId,
                    type = it.type,
                    referenceId = it.referenceId,
                    amount = it.amount,
                    appliedAt = it.createdAt,
                )
            }
    }
}
