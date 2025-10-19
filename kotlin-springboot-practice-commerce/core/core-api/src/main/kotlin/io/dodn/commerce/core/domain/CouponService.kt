package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.CouponTargetType
import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.storage.db.core.CouponRepository
import io.dodn.commerce.storage.db.core.CouponTargetRepository
import io.dodn.commerce.storage.db.core.ProductCategoryRepository
import org.springframework.stereotype.Service

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val couponTargetRepository: CouponTargetRepository,
    private val productCategoryRepository: ProductCategoryRepository,
) {
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
