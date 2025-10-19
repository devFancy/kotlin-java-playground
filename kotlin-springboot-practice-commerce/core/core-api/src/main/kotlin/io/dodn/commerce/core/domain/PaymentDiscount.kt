package io.dodn.commerce.core.domain

import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import java.math.BigDecimal

data class PaymentDiscount(
    private val ownedCoupons: List<OwnedCoupon>,
    private val pointBalance: PointBalance,
    val useOwnedCouponId: Long,
    private val usePointAmount: BigDecimal,
) {
    val couponDiscount: BigDecimal
    val usePoint: BigDecimal

    init {
        // 쿠폰 할인 계산
        couponDiscount = if (useOwnedCouponId > 0) {
            ownedCoupons.firstOrNull { it.id == useOwnedCouponId }?.coupon?.discount ?: throw CoreException(ErrorType.OWNED_COUPON_INVALID)
        } else {
            BigDecimal.ZERO
        }

        // 포인트 사용액 계산
        usePoint = if (usePointAmount > BigDecimal.ZERO) {
            if (usePointAmount > pointBalance.balance) throw CoreException(ErrorType.POINT_EXCEEDS_BALANCE)
            usePointAmount
        } else {
            BigDecimal.ZERO
        }
    }

    fun paidAmount(orderPrice: BigDecimal): BigDecimal {
        val amount = orderPrice - (couponDiscount + usePointAmount)
        if (amount < BigDecimal.ZERO) throw CoreException(ErrorType.PAYMENT_INVALID_AMOUNT)
        return amount
    }
}
