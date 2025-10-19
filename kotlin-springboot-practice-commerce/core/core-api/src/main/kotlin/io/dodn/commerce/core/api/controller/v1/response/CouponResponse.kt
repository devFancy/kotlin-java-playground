package io.dodn.commerce.core.api.controller.v1.response

import io.dodn.commerce.core.domain.Coupon
import io.dodn.commerce.core.enums.CouponType
import java.math.BigDecimal
import java.time.LocalDateTime

data class CouponResponse(
    val id: Long,
    val name: String,
    val type: CouponType,
    val discount: BigDecimal,
    val expiredAt: LocalDateTime,
) {
    companion object {
        fun of(coupon: Coupon): CouponResponse {
            return CouponResponse(
                id = coupon.id,
                name = coupon.name,
                type = coupon.type,
                discount = coupon.discount,
                expiredAt = coupon.expiredAt,
            )
        }
        fun of(coupons: List<Coupon>): List<CouponResponse> {
            return coupons.map { of(it) }
        }
    }
}
