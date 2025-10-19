package io.dodn.commerce.core.api.controller.v1.request

import io.dodn.commerce.core.domain.OwnedCoupon
import io.dodn.commerce.core.domain.PaymentDiscount
import io.dodn.commerce.core.domain.PointBalance
import java.math.BigDecimal

data class CreatePaymentRequest(
    val orderKey: String,
    val useOwnedCouponId: Long?,
    val usePoint: BigDecimal?,
) {
    fun toPaymentDiscount(ownedCoupons: List<OwnedCoupon>, pointBalance: PointBalance): PaymentDiscount {
        return PaymentDiscount(
            ownedCoupons = ownedCoupons,
            pointBalance = pointBalance,
            useOwnedCouponId = useOwnedCouponId ?: -1,
            usePointAmount = usePoint ?: BigDecimal.valueOf(-1),
        )
    }
}
