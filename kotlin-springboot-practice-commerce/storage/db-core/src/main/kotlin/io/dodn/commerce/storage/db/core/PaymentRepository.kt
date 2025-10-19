package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.PaymentState
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface PaymentRepository : JpaRepository<PaymentEntity, Long> {
    fun findByOrderId(orderId: Long): PaymentEntity?
    fun findAllByStateAndPaidAtBetween(state: PaymentState, from: LocalDateTime, to: LocalDateTime, pageable: Pageable): Slice<PaymentEntity>
}
