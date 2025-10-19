package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.OwnedCouponState

data class OwnedCoupon(
    val id: Long,
    val userId: Long,
    val state: OwnedCouponState,
    val coupon: Coupon,
)
