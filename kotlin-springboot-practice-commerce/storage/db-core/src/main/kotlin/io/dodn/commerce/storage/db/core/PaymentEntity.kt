package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.PaymentMethod
import io.dodn.commerce.core.enums.PaymentState
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(
    name = "payment",
    indexes = [
        Index(name = "udx_order_id", columnList = "orderId", unique = true),
    ],
)
class PaymentEntity(
    val userId: Long,
    val orderId: Long,
    val originAmount: BigDecimal,
    val ownedCouponId: Long,
    val couponDiscount: BigDecimal,
    val usedPoint: BigDecimal,
    val paidAmount: BigDecimal,
    state: PaymentState,
    externalPaymentKey: String? = null,
    method: PaymentMethod? = null,
    approveCode: String? = null,
    paidAt: LocalDateTime? = null,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var state: PaymentState = state
        protected set

    var externalPaymentKey: String? = externalPaymentKey
        protected set

    var method: PaymentMethod? = method
        protected set

    var approveCode: String? = approveCode
        protected set

    var paidAt: LocalDateTime? = paidAt
        protected set

    fun success(externalPaymentKey: String, method: PaymentMethod, approveCode: String) {
        this.state = PaymentState.SUCCESS
        this.externalPaymentKey = externalPaymentKey
        this.method = method
        this.approveCode = approveCode
        this.paidAt = LocalDateTime.now()
    }

    fun hasAppliedCoupon(): Boolean {
        return ownedCouponId > 0
    }
}
