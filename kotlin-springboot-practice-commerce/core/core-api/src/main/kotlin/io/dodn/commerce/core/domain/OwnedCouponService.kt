package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OwnedCouponService(
    private val couponRepository: CouponRepository,
    private val ownedCouponRepository: OwnedCouponRepository,
) {
    fun getOwnedCoupons(user: User): List<OwnedCoupon> {
        val ownedCoupons = ownedCouponRepository.findByUserIdAndStatus(user.id, EntityStatus.ACTIVE)
        if (ownedCoupons.isEmpty()) return emptyList()

        /**
         * Note:
         * - 쿠폰 데이터를 가져와서 맵핑시킨다.
         * 소유 쿠폰은 받은 것(userId, couponId)에 대한 것과 상태만 관리한다. -> 참고: OwnedCouponEntity
         * 쿠폰에 대한 유효기간, 할인금액, 타입은 마스터 테이블인 CouponEntity 에 있다.
         * 쿠폰의 대상을 추리는 테이블은 CouponTargetEntity 에 있다.
         * - Coupon ->(download)-> OwnedCoupon
         */
        val couponMap = couponRepository.findAllById(ownedCoupons.map { it.couponId }.toSet())
            .associateBy { it.id }

        return ownedCoupons.map {
            OwnedCoupon(
                id = it.id,
                userId = it.userId,
                state = it.state,
                coupon = Coupon(
                    id = couponMap[it.couponId]!!.id,
                    name = couponMap[it.couponId]!!.name,
                    type = couponMap[it.couponId]!!.type,
                    discount = couponMap[it.couponId]!!.discount,
                    expiredAt = couponMap[it.couponId]!!.expiredAt,
                ),
            )
        }
    }

    fun getOwnedCouponsForCheckout(user: User, productIds: Collection<Long>): List<OwnedCoupon> {
        if (productIds.isEmpty()) return emptyList()
        val applicableCouponMap = couponRepository.findApplicableCouponIds(productIds)
            .associateBy { it.id }

        if (applicableCouponMap.isEmpty()) return emptyList()
        val ownedCoupons = ownedCouponRepository.findOwnedCouponIds(user.id, applicableCouponMap.keys, LocalDateTime.now())

        if (ownedCoupons.isEmpty()) return emptyList()
        return ownedCoupons.map {
            OwnedCoupon(
                id = it.id,
                userId = it.userId,
                state = it.state,
                coupon = Coupon(
                    id = applicableCouponMap[it.couponId]!!.id,
                    name = applicableCouponMap[it.couponId]!!.name,
                    type = applicableCouponMap[it.couponId]!!.type,
                    discount = applicableCouponMap[it.couponId]!!.discount,
                    expiredAt = applicableCouponMap[it.couponId]!!.expiredAt,
                ),
            )
        }
    }
}
