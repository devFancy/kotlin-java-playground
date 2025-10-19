package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.EntityStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface CouponRepository : JpaRepository<CouponEntity, Long> {
    fun findByIdInAndStatus(ids: Collection<Long>, status: EntityStatus): List<CouponEntity>
    fun findByIdAndStatusAndExpiredAtAfter(couponId: Long, status: EntityStatus, expiredAtAfter: LocalDateTime): CouponEntity?

    @Query(
        """
         SELECT DISTINCT coupon FROM CouponEntity coupon
            LEFT JOIN CouponTargetEntity productTarget
                ON productTarget.couponId = coupon.id
                AND productTarget.targetType = 'PRODUCT'
                AND productTarget.targetId IN :productIds
                AND productTarget.status = 'ACTIVE'
            LEFT JOIN CouponTargetEntity categoryTarget
                ON categoryTarget.couponId = coupon.id
                AND categoryTarget.targetType = 'PRODUCT_CATEGORY'
                AND categoryTarget.status = 'ACTIVE'
            WHERE coupon.status = 'ACTIVE'
              AND (
                  productTarget.id IS NOT NULL
                  OR categoryTarget.targetId IN (
                      SELECT productCategory.categoryId
                      FROM ProductCategoryEntity productCategory
                      WHERE productCategory.productId IN :productIds
                        AND productCategory.status = 'ACTIVE'
                  )
              )
        """,
    )
    fun findApplicableCouponIds(productIds: Collection<Long>): List<CouponEntity>
}
