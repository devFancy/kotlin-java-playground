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

// 외부에 따라 달라지는 데이터들을 담는 테이블
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
    externalPaymentKey: String? = null, // PG 외부사의 key
    method: PaymentMethod? = null, // PG 외부사의 결제수단
    approveCode: String? = null, // PG 외부
    paidAt: LocalDateTime? = null, // PG 외부
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

    fun inProgress(externalPaymentKey: String) {
        this.state = PaymentState.IN_PROGRESS
        this.externalPaymentKey = externalPaymentKey
    }

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
