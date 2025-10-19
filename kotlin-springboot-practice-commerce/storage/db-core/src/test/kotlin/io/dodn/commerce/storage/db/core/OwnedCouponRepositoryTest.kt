package io.dodn.commerce.storage.db.core

import io.dodn.commerce.core.enums.CouponType
import io.dodn.commerce.core.enums.OwnedCouponState
import io.dodn.commerce.storage.db.CoreDbContextTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class OwnedCouponRepositoryTest(
    private val ownedCouponRepository: OwnedCouponRepository,
    private val couponRepository: CouponRepository,
) : CoreDbContextTest() {

    @Test
    fun `조건에_맞는_소유_쿠폰만_조회_되어야한다`() {
        // given
        val userId = 100L
        val now = LocalDateTime.now()

        // 활성 + 미만료 쿠폰 2개 생성
        val activeValid1 = couponRepository.save(
            CouponEntity(
                name = "ACTIVE_VALID_1",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.TEN,
                expiredAt = now.plusDays(7),
            ),
        )
        val activeValid2 = couponRepository.save(
            CouponEntity(
                name = "ACTIVE_VALID_2",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.ONE,
                expiredAt = now.plusDays(1),
            ),
        )

        // 비활성 쿠폰, 만료된 쿠폰 생성
        val inactiveCoupon = couponRepository.save(
            CouponEntity(
                name = "INACTIVE_COUPON",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.ONE,
                expiredAt = now.plusDays(3),
            ),
        ).also {
            it.delete()
            couponRepository.save(it)
        }
        val expiredCoupon = couponRepository.save(
            CouponEntity(
                name = "EXPIRED_COUPON",
                type = CouponType.FIXED_AMOUNT,
                discount = BigDecimal.ONE,
                expiredAt = now.minusDays(1),
            ),
        )

        // 다운로드 상태 소유 쿠폰 2개, 사용 상태 소유 쿠폰 1개, 다른 유저의 소유 쿠폰 1개 생성
        val ownedCoupon1 = ownedCouponRepository.save(
            OwnedCouponEntity(
                userId = userId,
                couponId = activeValid1.id,
                state = OwnedCouponState.DOWNLOADED,
            ),
        )
        val ownedCoupon2 = ownedCouponRepository.save(
            OwnedCouponEntity(
                userId = userId,
                couponId = activeValid2.id,
                state = OwnedCouponState.DOWNLOADED,
            ),
        )
        ownedCouponRepository.save(
            OwnedCouponEntity(
                userId = userId,
                couponId = inactiveCoupon.id,
                state = OwnedCouponState.USED,
            ),
        )
        ownedCouponRepository.save(
            OwnedCouponEntity(
                userId = userId,
                couponId = expiredCoupon.id,
                state = OwnedCouponState.DOWNLOADED,
            ),
        )
        ownedCouponRepository.save(
            OwnedCouponEntity(
                userId = 200L,
                couponId = activeValid1.id,
                state = OwnedCouponState.DOWNLOADED,
            ),
        )

        // when
        val result = ownedCouponRepository.findOwnedCouponIds(
            userId = userId,
            couponIds = listOf(activeValid1.id, activeValid2.id, inactiveCoupon.id, expiredCoupon.id),
            expiredAtAfter = now,
        )

        // then
        assertThat(result.map { it.id }.toSet()).containsExactlyInAnyOrder(ownedCoupon1.id, ownedCoupon2.id)
    }
}
