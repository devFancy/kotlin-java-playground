package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OwnedCouponState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.OwnedCouponEntity
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

    fun download(user: User, couponId: Long) {
        val coupon = couponRepository.findByIdAndStatusAndExpiredAtAfter(couponId, EntityStatus.ACTIVE, LocalDateTime.now())
            ?: throw CoreException(ErrorType.COUPON_NOT_FOUND_OR_EXPIRED)

        val existing = ownedCouponRepository.findByUserIdAndCouponId(user.id, couponId)
        if (existing != null) {
            throw CoreException(ErrorType.COUPON_ALREADY_DOWNLOADED)
        }
        ownedCouponRepository.save(
            OwnedCouponEntity(
                userId = user.id,
                couponId = coupon.id,
                state = OwnedCouponState.DOWNLOADED,
            ),
        )
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
