package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.OwnedCouponState
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Index
import jakarta.persistence.Table
import jakarta.persistence.Version

@Entity
@Table(
    name = "owned_coupon",
    indexes = [
        Index(name = "udx_owned_coupon", columnList = "userId, couponId", unique = true),
    ],
)
class OwnedCouponEntity(
    val userId: Long,
    val couponId: Long,
    state: OwnedCouponState,
    @Version
    private var version: Long = 0,
) : BaseEntity() {
    @Enumerated(EnumType.STRING)
    var state: OwnedCouponState = state
        protected set

    fun use() {
        state = OwnedCouponState.USED
    }

    fun revert() {
        state = OwnedCouponState.DOWNLOADED
    }
}
