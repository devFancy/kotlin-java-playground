package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.CouponTargetType
import io.dodn.commerce.core.enums.CouponType
import io.dodn.commerce.storage.db.CoreDbContextTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class CouponRepositoryTest(
    private val couponRepository: CouponRepository,
    private val couponTargetRepository: CouponTargetRepository,
    private val productCategoryRepository: ProductCategoryRepository,
) : CoreDbContextTest() {

    @Test
    fun `활성_쿠폰_조회가_되어야한다`() {
        // given
        val now = LocalDateTime.now()

        // 상품 10에 직접 적용되는 쿠폰
        val c1 = couponRepository.save(
            CouponEntity(
                name = "PRODUCT_ID_10",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.TEN,
                expiredAt = now.plusDays(7),
            ),
        )
        couponTargetRepository.save(
            CouponTargetEntity(
                couponId = c1.id,
                targetType = CouponTargetType.PRODUCT,
                targetId = 10L,
            ),
        )

        // 카테고리 100에 적용되는 쿠폰, 상품 11은 카테고리 100에 속함
        val c2 = couponRepository.save(
            CouponEntity(
                name = "PRODUCT_CATEGORY_100",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.ONE,
                expiredAt = now.plusDays(3),
            ),
        )
        couponTargetRepository.save(
            CouponTargetEntity(
                couponId = c2.id,
                targetType = CouponTargetType.PRODUCT_CATEGORY,
                targetId = 100L,
            ),
        )
        productCategoryRepository.save(
            ProductCategoryEntity(
                productId = 11L,
                categoryId = 100L,
            ),
        )

        // 매치될 수 있지만 삭제(비활성)된 쿠폰
        val c3 = couponRepository.save(
            CouponEntity(
                name = "INACTIVE_COUPON_DELETED",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.valueOf(5),
                expiredAt = now.plusDays(5),
            ),
        ).also {
            it.delete()
            couponRepository.save(it)
        }
        couponTargetRepository.save(
            CouponTargetEntity(
                couponId = c3.id,
                targetType = CouponTargetType.PRODUCT,
                targetId = 12L,
            ),
        )

        // 동일 상품에 대해 상품과 카테고리 둘 다 적용되는 쿠폰 (중복 없이 유니크하게 처리되어야 함)
        val c4 = couponRepository.save(
            CouponEntity(
                name = "BOTH_PRODUCT_12_PRODUCT_CATEGORY_200",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.valueOf(20),
                expiredAt = now.plusDays(10),
            ),
        )
        couponTargetRepository.saveAll(
            listOf(
                CouponTargetEntity(
                    couponId = c4.id,
                    targetType = CouponTargetType.PRODUCT,
                    targetId = 12L,
                ),
                CouponTargetEntity(
                    couponId = c4.id,
                    targetType = CouponTargetType.PRODUCT_CATEGORY,
                    targetId = 200L,
                ),
            ),
        )
        productCategoryRepository.save(
            ProductCategoryEntity(
                productId = 12L,
                categoryId = 200L,
            ),
        )

        // 비활성 타깃은 쿠폰 적용 대상이 아님
        val c5 = couponRepository.save(
            CouponEntity(
                name = "INACTIVE_COUPON_TARGET_DELETED",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.valueOf(7),
                expiredAt = now.plusDays(2),
            ),
        )
        couponTargetRepository.save(
            CouponTargetEntity(
                couponId = c5.id,
                targetType = CouponTargetType.PRODUCT,
                targetId = 13L,
            ),
        ).also {
            it.delete()
            couponTargetRepository.save(it)
        }

        // 쿠폰만 존재하는 데이터
        couponRepository.save(
            CouponEntity(
                name = "NOT_MATCH_PRODUCT",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.ONE,
                expiredAt = now.plusDays(1),
            ),
        )

        // when
        val result = couponRepository.findApplicableCouponIds(listOf(10L, 11L, 12L, 13L))

        // then
        assertThat(result.map { it.id }.toSet()).containsExactlyInAnyOrder(c1.id, c2.id, c4.id)
    }
}
