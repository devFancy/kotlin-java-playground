package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CouponTargetType
import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.enums.OwnedCouponState
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.CouponTargetRepository
import io.dodn.commerce.storage.db.core.OwnedCouponEntity
import io.dodn.commerce.storage.db.core.OwnedCouponRepository
import io.dodn.commerce.storage.db.core.ProductCategoryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val couponTargetRepository: CouponTargetRepository,
    private val productCategoryRepository: ProductCategoryRepository,
    private val ownedCouponRepository: OwnedCouponRepository,
) {

    /**
     * Note:
     * - 여기서 질문: 다운로드라는 이 행위 자체는 누구한테 있는게 맞을까?
     * -> 'download' 부분은 사실 CouponService 에 있는게 맞다고 본다.
     * -> Why? 쿠폰을 다운로드 받아야만 OwnedCoupon 이 생기기 때문.
     */
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

    /**
     * Note:
     * 유저가 이 상품에 대해서 다운로드 받을 수 있는 쿠폰을 조회하는 로직
     */
    fun getCouponsForProducts(productIds: Collection<Long>): List<Coupon> {
        val productTargets = couponTargetRepository.findByTargetTypeAndTargetIdInAndStatus(
            CouponTargetType.PRODUCT,
            productIds,
            EntityStatus.ACTIVE,
        )
        val categoryTargets = couponTargetRepository.findByTargetTypeAndTargetIdInAndStatus(
            CouponTargetType.PRODUCT_CATEGORY,
            productCategoryRepository.findByProductIdInAndStatus(productIds, EntityStatus.ACTIVE).map { it.categoryId },
            EntityStatus.ACTIVE,
        )
        return couponRepository.findByIdInAndStatus((productTargets + categoryTargets).map { it.couponId }.toSet(), EntityStatus.ACTIVE)
            .map {
                Coupon(
                    id = it.id,
                    name = it.name,
                    type = it.type,
                    discount = it.discount,
                    expiredAt = it.expiredAt,
                )
            }
    }
}
