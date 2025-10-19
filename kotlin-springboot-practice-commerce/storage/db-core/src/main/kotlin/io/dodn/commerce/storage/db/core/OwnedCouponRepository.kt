package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface OwnedCouponRepository : JpaRepository<OwnedCouponEntity, Long> {
    fun findByUserIdAndCouponId(userId: Long, couponId: Long): OwnedCouponEntity?
    fun findByUserIdAndStatus(userId: Long, status: EntityStatus): List<OwnedCouponEntity>

    @Query(
        """
        SELECT DISTINCT ownedCoupon FROM OwnedCouponEntity ownedCoupon
            JOIN CouponEntity coupon
                ON ownedCoupon.couponId = coupon.id
                AND ownedCoupon.userId = :userId
                AND ownedCoupon.state = 'DOWNLOADED'
                AND ownedCoupon.status = 'ACTIVE'
        WHERE 
            coupon.id IN :couponIds
            AND coupon.status = 'ACTIVE'
            AND coupon.expiredAt > :expiredAtAfter            
        """,
    )
    fun findOwnedCouponIds(
        userId: Long,
        couponIds: Collection<Long>,
        expiredAtAfter: LocalDateTime,
    ): List<OwnedCouponEntity>
}
