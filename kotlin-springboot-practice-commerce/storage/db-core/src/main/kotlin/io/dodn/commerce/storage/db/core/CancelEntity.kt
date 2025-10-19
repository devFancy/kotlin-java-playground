package io.dodn.commerce.storage.db.core

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "cancel")
class CancelEntity(
    val userId: Long,
    val orderId: Long,
    val paymentId: Long,
    val originAmount: BigDecimal,
    val ownedCouponId: Long,
    val couponDiscount: BigDecimal,
    val usedPoint: BigDecimal,
    val paidAmount: BigDecimal,
    val canceledAmount: BigDecimal,
    val externalCancelKey: String,
    val canceledAt: LocalDateTime,
) : BaseEntity()
