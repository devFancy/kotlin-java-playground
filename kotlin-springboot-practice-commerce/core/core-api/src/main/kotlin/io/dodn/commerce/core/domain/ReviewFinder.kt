package io.dodn.commerce.core.domain

import io.dodn.commerce.core.enums.EntityStatus
import io.dodn.commerce.core.support.OffsetLimit
import io.dodn.commerce.core.support.Page
import io.dodn.commerce.storage.db.core.ReviewRepository
import org.springframework.stereotype.Component

@Component
class ReviewFinder(
    private val reviewRepository: ReviewRepository,
) {
    fun find(target: ReviewTarget, offsetLimit: OffsetLimit): Page<Review> {
        val result = reviewRepository.findByTargetTypeAndTargetIdAndStatus(
            target.type,
            target.id,
            EntityStatus.ACTIVE,
            offsetLimit.toPageable(),
        )
        return Page(
            result.content.map {
                Review(
                    id = it.id,
                    userId = it.userId,
                    ReviewTarget(
                        type = it.targetType,
                        id = it.targetId,
                    ),
                    ReviewContent(
                        rate = it.rate,
                        content = it.content,
                    ),
                )
            },
            result.hasNext(),
        )
    }

    /**
     * Note:
     * 특정 대상(target)의 '활성 리뷰'만 집계하여 평균 평점과 총 개수를 반환한다.
     * - 활성된 리뷰의 평점(rate) 합계를 구한 뒤, 리뷰 수로 나누어 평균을 계산한다.
     */
    fun findRateSummary(target: ReviewTarget): RateSummary {
        val founds = reviewRepository.findByTargetTypeAndTargetId(target.type, target.id).filter { it.isActive() }
        return if (founds.isEmpty()) {
            RateSummary.EMPTY
        } else {
            RateSummary(
                rate = founds.sumOf { it.rate }.divide(founds.size.toBigDecimal()),
                count = founds.size.toLong(),
            )
        }
    }
}
